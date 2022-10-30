package com.andreikslpv.filmfinder.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel
import com.andreikslpv.filmfinder.presentation.MainActivity
import com.andreikslpv.filmfinder.presentation.TRANSITION_DURATION
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailsFragment : Fragment() {
    private lateinit var film: FilmsLocalModel

    init {
        enterTransition = Fade(Fade.IN).apply { duration = TRANSITION_DURATION }
        returnTransition = Fade(Fade.OUT).apply { duration = TRANSITION_DURATION }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Получаем наш фильм из переданного бандла
        film = arguments?.get("film") as FilmsLocalModel

        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(film)
        initIconFavorites()
        initIconWatchLater()
        initIconShare()
    }

    private fun initView(film: FilmsLocalModel) {
        //Приостанавливаем воспроизведение Transition до загрузки данных
        postponeEnterTransition()
        //Устанавливаем заголовок
        val detailsToolbar = requireView().findViewById<Toolbar>(R.id.details_toolbar)
        detailsToolbar.title = film.title
        //Устанавливаем картинку
        val detailsPoster = requireView().findViewById<ImageView>(R.id.details_poster)
        //Указываем контейнер, в котором будет "жить" картинка
        Glide.with(requireView())
            //Загружаем сам ресурс
            .load(film.poster)
            //Центруем изображение
            .centerCrop()
            //Указываем ImageView, куда будем загружать изображение
            .into(detailsPoster)
        //Устанавливаем описание
        val detailsDescription = requireView().findViewById<TextView>(R.id.details_description)
        detailsDescription.text = film.description
        //Данные загружены, запускаем анимацию перехода
        startPostponedEnterTransition()
    }

    private fun initIconFavorites() {
        val iconFavorites =
            requireView().findViewById<FloatingActionButton>(R.id.details_fab_favorites)
        iconFavorites.setImageResource(
            if (film.isFavorite) R.drawable.ic_baseline_favorite
            else R.drawable.ic_baseline_favorite_border
        )
        iconFavorites.setOnClickListener {
            if (!film.isFavorite) {
                iconFavorites.setImageResource(R.drawable.ic_baseline_favorite)
                film.isFavorite = true
            } else {
                iconFavorites.setImageResource(R.drawable.ic_baseline_favorite_border)
                film.isFavorite = false
            }
            (activity as MainActivity).filmsRepository.changeFilmLocalState(film)
        }
    }

    private fun initIconWatchLater() {
        val iconWatchLater =
            requireView().findViewById<FloatingActionButton>(R.id.details_fab_watch_later)
        iconWatchLater.setImageResource(
            if (film.isWatchLater) R.drawable.ic_baseline_watch_later
            else R.drawable.ic_baseline_watch_later_border
        )
        iconWatchLater.setOnClickListener {
            if (!film.isWatchLater) {
                iconWatchLater.setImageResource(R.drawable.ic_baseline_watch_later)
                film.isWatchLater = true
            } else {
                iconWatchLater.setImageResource(R.drawable.ic_baseline_watch_later_border)
                film.isWatchLater = false
            }
            (activity as MainActivity).filmsRepository.changeFilmLocalState(film)
        }
    }

    private fun initIconShare() {
        val iconShare =
            requireView().findViewById<FloatingActionButton>(R.id.details_fab_share)
        iconShare.setOnClickListener {
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