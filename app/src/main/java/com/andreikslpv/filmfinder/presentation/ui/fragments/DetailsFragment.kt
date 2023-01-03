package com.andreikslpv.filmfinder.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.FragmentDetailsBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.presentation.ui.BUNDLE_KEY_FILM
import com.andreikslpv.filmfinder.presentation.ui.BUNDLE_KEY_TYPE
import com.andreikslpv.filmfinder.presentation.ui.MainActivity
import com.andreikslpv.filmfinder.presentation.ui.TRANSITION_DURATION
import com.andreikslpv.filmfinder.presentation.ui.utils.FragmentsType
import com.andreikslpv.filmfinder.presentation.ui.utils.loadImage
import com.andreikslpv.filmfinder.presentation.vm.DetailsFragmentViewModel

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!
    private var currentId: Int = -1
    private var message: String = ""
    private lateinit var type: FragmentsType
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DetailsFragmentViewModel::class.java)
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

        viewModel.filmLiveData.observe(viewLifecycleOwner) {
            binding.detailsFabFavorites.setImageResource(
                if (it.isFavorite) R.drawable.ic_baseline_favorite
                else R.drawable.ic_baseline_favorite_border
            )
            binding.detailsFabWatchLater.setImageResource(
                if (it.isWatchLater) R.drawable.ic_baseline_watch_later
                else R.drawable.ic_baseline_watch_later_border
            )
            // для того, чтобы при смене значений полей isFavorites & isWatchlater
            // не переустанавливать все ресурсы проверяем сменился ли фильм (поменялся его id)
            if (currentId != it.id) {
                currentId = it.id
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
        setBackground()
        //Данные загружены, запускаем анимацию перехода
        startPostponedEnterTransition()
        initIcons()
    }

    override fun onPause() {
        super.onPause()
        // сбрасываем сохраненный id для того чтобы данные фильма загрузились
        // в случае backpress и повторного вызова данного фильма
        currentId = -1
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

    private fun initIcons() {
        binding.detailsFabFavorites.setOnClickListener {
            viewModel.changeFavoritesField()
        }
        binding.detailsFabWatchLater.setOnClickListener {
            viewModel.changeWatchLaterField()
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

}