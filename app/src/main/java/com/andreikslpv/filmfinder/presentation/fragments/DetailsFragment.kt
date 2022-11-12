package com.andreikslpv.filmfinder.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.FragmentDetailsBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.ChangeFilmLocalStateUseCase
import com.andreikslpv.filmfinder.presentation.FragmentsType
import com.andreikslpv.filmfinder.presentation.MainActivity
import com.andreikslpv.filmfinder.presentation.TRANSITION_DURATION
import com.bumptech.glide.Glide

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!
    private val changeFilmLocalStateUseCase by lazy {
        ChangeFilmLocalStateUseCase((activity as MainActivity).filmsRepository)
    }
    private lateinit var film: FilmDomainModel
    private lateinit var type: FragmentsType

    init {
        enterTransition = Fade(Fade.IN).apply { duration = TRANSITION_DURATION }
        returnTransition = Fade(Fade.OUT).apply { duration = TRANSITION_DURATION }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Получаем фильм и тип фрагмента (из которого вызван фрагмент) из переданного бандла
        film = arguments?.get("film") as FilmDomainModel
        type = arguments?.get("type") as FragmentsType
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(film)
        initIconFavorites()
        initIconWatchLater()
        initIconShare()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView(film: FilmDomainModel) {
        //Приостанавливаем воспроизведение Transition до загрузки данных
        postponeEnterTransition()
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
            FragmentsType.DETAILS -> binding.detailsFragmentRoot.background =
                ResourcesCompat.getDrawable(resources, R.drawable.background_details, null)
        }
        //Устанавливаем заголовок
        binding.detailsToolbar.title = film.title
        //Указываем контейнер, в котором будет "жить" картинка
        Glide.with(requireView())
            //Загружаем сам ресурс
            .load(film.poster)
            //Центруем изображение
            .centerCrop()
            //Указываем ImageView, куда будем загружать изображение
            .into(binding.detailsPoster)
        //Устанавливаем описание
        binding.detailsDescription.text = film.description
        //Данные загружены, запускаем анимацию перехода
        startPostponedEnterTransition()
    }

    private fun initIconFavorites() {
        binding.detailsFabFavorites.setImageResource(
            if (film.isFavorite) R.drawable.ic_baseline_favorite
            else R.drawable.ic_baseline_favorite_border
        )
        binding.detailsFabFavorites.setOnClickListener {
            if (!film.isFavorite) {
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_baseline_favorite)
                film.isFavorite = true
            } else {
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_baseline_favorite_border)
                film.isFavorite = false
            }
            changeFilmLocalStateUseCase.execute(film)
        }
    }

    private fun initIconWatchLater() {
        binding.detailsFabWatchLater.setImageResource(
            if (film.isWatchLater) R.drawable.ic_baseline_watch_later
            else R.drawable.ic_baseline_watch_later_border
        )
        binding.detailsFabWatchLater.setOnClickListener {
            if (!film.isWatchLater) {
                binding.detailsFabWatchLater.setImageResource(R.drawable.ic_baseline_watch_later)
                film.isWatchLater = true
            } else {
                binding.detailsFabWatchLater.setImageResource(R.drawable.ic_baseline_watch_later_border)
                film.isWatchLater = false
            }
            changeFilmLocalStateUseCase.execute(film)
        }
    }

    private fun initIconShare() {
        binding.detailsFabShare.setOnClickListener {
            //Создаем интент
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this film: ${film.title} \n\n ${film.description}"
            )
            //Указываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
    }
}