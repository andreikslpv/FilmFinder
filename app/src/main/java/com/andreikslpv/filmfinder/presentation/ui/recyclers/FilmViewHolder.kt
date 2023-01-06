package com.andreikslpv.filmfinder.presentation.ui.recyclers

import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.filmfinder.databinding.ItemFilmBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.presentation.ui.utils.loadImage

class FilmViewHolder(val binding: ItemFilmBinding) : RecyclerView.ViewHolder(binding.root) {

    // кладем данные из Film в наши View
    fun bind(film: FilmDomainModel) {
        //Устанавливаем заголовок
        binding.title.text = film.title
        //Устанавливаем постер (маленький)
        binding.poster.loadImage(film.posterPreview)
        //Устанавливаем описание
        binding.description.text = film.description
        //Устанавливаем рейтинг
        binding.ratingDonut.setProgressWithAnimation((film.rating * 10).toInt())
    }
}