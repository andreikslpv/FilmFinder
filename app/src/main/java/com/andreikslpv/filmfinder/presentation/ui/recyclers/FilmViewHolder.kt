package com.andreikslpv.filmfinder.presentation.ui.recyclers

import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.ItemFilmBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.presentation.ui.utils.loadImage
import com.andreikslpv.filmfinder.presentation.ui.utils.toTime

class FilmViewHolder(val binding: ItemFilmBinding) : RecyclerView.ViewHolder(binding.root) {

    // кладем данные из Film в наши View
    fun bind(film: FilmDomainModel, isWatchLaterRecycler: Boolean) {
        //Устанавливаем заголовок
        binding.title.text = film.title
        //Устанавливаем постер (маленький)
        binding.poster.loadImage(film.posterPreview)
        //Устанавливаем описание или время напоминания
        if (isWatchLaterRecycler) {
            if (film.reminderTime > System.currentTimeMillis()) {
                binding.description.text =
                    binding.root.context.getString(
                        R.string.item_reminder_not_null,
                        film.reminderTime.toTime(binding.root.context)
                    )
            } else {
                binding.description.text =
                    binding.root.context.getString(R.string.item_reminder_null)
            }
            binding.description.gravity = Gravity.BOTTOM
        } else {
            binding.description.text = film.description
            binding.description.gravity = Gravity.TOP
        }
        //Устанавливаем рейтинг
        binding.ratingDonut.setProgressWithAnimation((film.rating * 10).toInt())
    }

}