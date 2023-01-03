package com.andreikslpv.filmfinder.presentation.ui.recyclers

import android.widget.ImageView
import android.widget.TextView
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.presentation.ui.customviews.RatingDonutView

//Интерфейс для обработки кликов
interface FilmOnItemClickListener {
    fun click(film: FilmDomainModel, image: ImageView, text: TextView, rating: RatingDonutView)
}