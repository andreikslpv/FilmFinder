package com.andreikslpv.filmfinder.presentation.ui.fragments

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.FragmentDetailsBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.presentation.ui.BUNDLE_KEY_FILM
import com.andreikslpv.filmfinder.presentation.ui.BUNDLE_KEY_TYPE
import com.andreikslpv.filmfinder.presentation.ui.MainActivity
import com.andreikslpv.filmfinder.presentation.ui.TRANSITION_DURATION
import com.andreikslpv.filmfinder.presentation.ui.utils.FragmentsType
import com.andreikslpv.filmfinder.presentation.ui.utils.loadImage
import com.andreikslpv.filmfinder.presentation.ui.utils.makeToast
import com.andreikslpv.filmfinder.presentation.vm.DetailsFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!
    private var message: String = ""
    private lateinit var type: FragmentsType
    private val viewModel: DetailsFragmentViewModel by viewModels()
    private val scope = CoroutineScope(Dispatchers.IO)
    private val singlePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    // доступ к хранилищу разрешен, открываем камеру
                    performAsyncLoadOfPoster()
                }
                !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    // доступ к хранилищу запрещен, пользователь поставил галочку Don't ask again.
                    // сообщаем пользователю, что он может в дальнейшем разрешить доступ
                    getString(R.string.details_allow_later_in_settings).makeToast(requireContext())
                }
                else -> {
                    // доступ к хранилищу запрещен, пользователь отклонил запрос
                }
            }
        }

    init {
        enterTransition = Fade(Fade.IN).apply { duration = TRANSITION_DURATION }
        returnTransition = Fade(Fade.OUT).apply { duration = TRANSITION_DURATION }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Получаем фильм и тип фрагмента (из которого вызван фрагмент) из переданного бандла
        viewModel.setFilm(arguments?.get(BUNDLE_KEY_FILM) as FilmDomainModel)
        type = arguments?.get(BUNDLE_KEY_TYPE) as FragmentsType
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Приостанавливаем воспроизведение Transition до загрузки данных
        postponeEnterTransition()
        observeFilmLocalState()
        setBackground()
        initIcons()
        //Данные загружены, запускаем анимацию перехода
        startPostponedEnterTransition()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeFilmLocalState() {
        viewModel.filmLocalStateLiveData.observe(viewLifecycleOwner) {
            setFavoritesIcon(it.isFavorite)
            setWatchLaterIcon(it.isWatchLater)
            // формируем сообщение, которое будет использоваться при share
            message = resources.getString(R.string.details_share_message) + it.title
            //Устанавливаем заголовок
            binding.detailsToolbar.title = it.title
            // Устанавливаем постер фильма (большой)
            binding.detailsPoster.loadImage(it.posterDetails)
            //Устанавливаем описание
            binding.detailsDescription.text = it.description
            //Устанавливаем рейтинг
            binding.detailsRatingDonut.progress = (it.rating * 10).toInt()
        }
    }

    private fun setBackground() {
        // устанавливаем background в зависимости от типа фрагмента, из которого вызван фрагмент Details
        when (type) {
            FragmentsType.HOME -> binding.detailsFragmentRoot.background =
                ResourcesCompat.getDrawable(resources, R.drawable.background_home, null)
            FragmentsType.FAVORITES -> binding.detailsFragmentRoot.background =
                ResourcesCompat.getDrawable(resources, R.drawable.background_favorites, null)
            FragmentsType.WATCH_LATER -> binding.detailsFragmentRoot.background =
                ResourcesCompat.getDrawable(resources, R.drawable.background_watch_later, null)
            FragmentsType.SELECTIONS -> binding.detailsFragmentRoot.background =
                ResourcesCompat.getDrawable(resources, R.drawable.background_selections, null)
            else -> binding.detailsFragmentRoot.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.background_details,
                    (activity as MainActivity).theme
                )
        }
    }

    private fun setFavoritesIcon(isEnable: Boolean) {
        binding.detailsFabFavorites.setImageResource(
            if (isEnable) R.drawable.ic_baseline_favorite
            else R.drawable.ic_baseline_favorite_border
        )
    }

    private fun setWatchLaterIcon(isEnable: Boolean) {
        binding.detailsFabWatchLater.setImageResource(
            if (isEnable) R.drawable.ic_baseline_watch_later
            else R.drawable.ic_baseline_watch_later_border
        )
    }

    private fun initIcons() {
        binding.detailsFabFavorites.setOnClickListener {
            viewModel.changeFavoritesField()
            setFavoritesIcon(viewModel.filmLocalStateLiveData.value?.isFavorite ?: false)
        }
        binding.detailsFabWatchLater.setOnClickListener {
            viewModel.changeWatchLaterField()
            setWatchLaterIcon(viewModel.filmLocalStateLiveData.value?.isWatchLater ?: false)
        }
        binding.detailsFabDownloadPoster.setOnClickListener {
            // если Андройд 10+ то сразу запускаем скачивание, иначе запрашиваем разрешение
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                performAsyncLoadOfPoster()
                return@setOnClickListener
            }
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // доступ к хранилищу запрещен, нужно объяснить зачем нам требуется разрешение
                singlePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                singlePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
        binding.detailsFabShare.setOnClickListener {
            //Создаем интент
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(Intent.EXTRA_TEXT, message)
            //Указываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(
                Intent.createChooser(
                    intent,
                    resources.getString(R.string.details_share_title)
                )
            )
        }
    }

    // ------------- сохранение постера
    private fun performAsyncLoadOfPoster() {
        //Создаем родительский скоуп с диспатчером Main потока, так как будем взаимодействовать с UI
        MainScope().launch {
            //Включаем Прогресс-бар
            binding.detailsProgressBar.isVisible = true
            //Создаем через async, так как нам нужен результат от работы, то есть Bitmap
            val job = scope.async {
                viewModel.loadWallpaper(viewModel.filmLocalStateLiveData.value!!.posterDetails)
            }
            //Сохраняем в галерею, как только файл загрузится
            saveToGallery(job.await())
            //Выводим снекбар с кнопкой перейти в галерею
            Snackbar.make(
                binding.root,
                R.string.details_downloaded_to_gallery,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.details_open) {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.type = "image/*"
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                .show()

            //Отключаем Прогресс-бар
            binding.detailsProgressBar.isVisible = false
        }
    }

    private fun saveToGallery(bitmap: Bitmap) {
        //Проверяем версию системы
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //Создаем объект для передачи данных
            val contentValues = ContentValues().apply {
                //Составляем информацию для файла (имя, тип, дата создания, куда сохранять и т.д.)
                put(
                    MediaStore.Images.Media.TITLE,
                    viewModel.filmLocalStateLiveData.value!!.title.handleSingleQuote()
                )
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    viewModel.filmLocalStateLiveData.value!!.title.handleSingleQuote()
                )
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.Images.Media.DATE_ADDED,
                    System.currentTimeMillis() / 1000
                )
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FilmsSearchApp")
            }
            //Получаем ссылку на объект Content resolver, который помогает передавать информацию из приложения вовне
            val contentResolver = requireActivity().contentResolver
            val uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            //Открываем канал для записи на диск
            val outputStream = contentResolver.openOutputStream(uri!!)
            //Передаем нашу картинку, может сделать компрессию
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            //Закрываем поток
            outputStream?.close()
        } else {
            //То же, но для более старых версий ОС
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.insertImage(
                requireActivity().contentResolver,
                bitmap,
                viewModel.filmLocalStateLiveData.value!!.title.handleSingleQuote(),
                viewModel.filmLocalStateLiveData.value!!.description.handleSingleQuote()
            )
        }
    }

    private fun String.handleSingleQuote(): String {
        return this.replace("'", "")
    }

}