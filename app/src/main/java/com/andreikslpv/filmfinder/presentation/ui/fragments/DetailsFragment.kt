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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.FragmentDetailsBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.presentation.notifications.NotificationConstants.DEFAULT_TIME
import com.andreikslpv.filmfinder.presentation.notifications.NotificationHelper
import com.andreikslpv.filmfinder.presentation.notifications.ReminderCallback
import com.andreikslpv.filmfinder.presentation.ui.BUNDLE_KEY_FILM
import com.andreikslpv.filmfinder.presentation.ui.BUNDLE_KEY_TYPE
import com.andreikslpv.filmfinder.presentation.ui.MainActivity
import com.andreikslpv.filmfinder.presentation.ui.TRANSITION_DURATION
import com.andreikslpv.filmfinder.presentation.ui.utils.*
import com.andreikslpv.filmfinder.presentation.vm.DetailsFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: DetailsFragmentViewModel by viewModels()
    private val autoDisposable = AutoDisposable()

    private var message: String = ""
    private lateinit var type: FragmentsType

    private lateinit var singlePermissionPostNotifications: ActivityResultLauncher<String>
    private lateinit var singlePermissionWriteExternalStorage: ActivityResultLauncher<String>

    init {
        enterTransition = Fade(Fade.IN).apply { duration = TRANSITION_DURATION }
        returnTransition = Fade(Fade.OUT).apply { duration = TRANSITION_DURATION }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // привязываемся к ЖЦ компонента
        autoDisposable.bindTo(lifecycle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Получаем фильм и тип фрагмента (из которого вызван фрагмент) из переданного бандла
        val film = arguments?.get(BUNDLE_KEY_FILM) as FilmDomainModel
        type = arguments?.get(BUNDLE_KEY_TYPE) as FragmentsType
        viewModel.setFilm(film)

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Приостанавливаем воспроизведение Transition до загрузки данных
        postponeEnterTransition()

        initRegisterForActivityResult()
        observeFilmLocalState()
        setBackground()
        initIcons()

        //Данные загружены, запускаем анимацию перехода
        startPostponedEnterTransition()
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearPrevFilm()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initRegisterForActivityResult() {
        singlePermissionPostNotifications =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    when {
                        granted -> {
                            // уведомления разрешены
                            setNotification()
                        }
                        !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                            // уведомления запрещены, пользователь поставил галочку Don't ask again.
                            // сообщаем пользователю, что он может в дальнейшем разрешить уведомления
                            getString(R.string.details_allow_later_post_notifications).makeToast(
                                requireContext()
                            )
                        }
                        else -> {
                            // уведомления запрещены, пользователь отклонил запрос
                        }
                    }
                }
            }

        singlePermissionWriteExternalStorage =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                when {
                    granted -> {
                        // доступ к хранилищу разрешен, начинаем загрузку
                        performAsyncLoadOfPoster()
                    }
                    !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                        // доступ к хранилищу запрещен, пользователь поставил галочку Don't ask again.
                        // сообщаем пользователю, что он может в дальнейшем разрешить доступ
                        getString(R.string.details_allow_later_write_external_storage).makeToast(
                            requireContext()
                        )
                    }
                    else -> {
                        // доступ к хранилищу запрещен, пользователь отклонил запрос
                    }
                }
            }
    }

    private fun setNotification() {
        NotificationHelper.notificationSet(
            requireContext(),
            viewModel.prevFilm,
            object : ReminderCallback {
                override fun onSuccess(reminderTime: Long) {
                    viewModel.changeWatchLaterField(reminderTime)
                }

                override fun onFailure() {
                }
            })

    }

    private fun observeFilmLocalState() {
        viewModel.filmLocalState.filter {
            it.id == viewModel.prevFilm.id
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
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
            .addTo(autoDisposable)
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
        }

        binding.detailsFabWatchLater.setOnClickListener {
            if (!viewModel.prevFilm.isWatchLater)
            // если Андройд 13+ то запрашиваем разрешение на показ уведомлений
                if (Build.VERSION.SDK_INT >= 33) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                        // уведомления запрещены, нужно объяснить зачем нам требуется разрешение
                        singlePermissionPostNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        singlePermissionPostNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                } else {
                    setNotification()
                }
            else {
                viewModel.changeWatchLaterField(DEFAULT_TIME)
            }
        }

        binding.detailsFabDownloadPoster.setOnClickListener {
            // если Андройд 10+ то сразу запускаем скачивание, иначе запрашиваем разрешение
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                performAsyncLoadOfPoster()
                return@setOnClickListener
            }
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // доступ к хранилищу запрещен, нужно объяснить зачем нам требуется разрешение
                singlePermissionWriteExternalStorage.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                singlePermissionWriteExternalStorage.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        binding.detailsProgressBar.isVisible = true

        val poster: Observable<Bitmap> = Observable.create { o ->
            o.onNext(viewModel.loadWallpaper(viewModel.prevFilm.posterDetails))
            o.onComplete()
        }

        poster
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    //Сохраняем в галерею, как только файл загрузится
                    saveToGallery(it)
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
                },
                { it.message?.makeToast(requireContext()) }
            ) {
                // onComplete Отключаем Прогресс-бар
                binding.detailsProgressBar.isVisible = false
            }
            .addTo(autoDisposable)
    }

    private fun saveToGallery(bitmap: Bitmap) {
        //Проверяем версию системы
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //Создаем объект для передачи данных
            val contentValues = ContentValues().apply {
                //Составляем информацию для файла (имя, тип, дата создания, куда сохранять и т.д.)
                put(
                    MediaStore.Images.Media.TITLE,
                    viewModel.prevFilm.title.handleSingleQuote()
                )
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    viewModel.prevFilm.title.handleSingleQuote()
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
                viewModel.prevFilm.title.handleSingleQuote(),
                viewModel.prevFilm.description.handleSingleQuote()
            )
        }
    }

    private fun String.handleSingleQuote(): String {
        return this.replace("'", "")
    }

}