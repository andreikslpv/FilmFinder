package com.andreikslpv.filmfinder.presentation.ui.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.andreikslpv.filmfinder.databinding.ItemFilmBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.presentation.ui.TRANSITION_NAME_FOR_IMAGE
import com.andreikslpv.filmfinder.presentation.ui.TRANSITION_NAME_FOR_RATING
import com.andreikslpv.filmfinder.presentation.ui.TRANSITION_NAME_FOR_TEXT

class FilmPagingAdapter(private val clickListener: FilmOnItemClickListener) :
    PagingDataAdapter<FilmDomainModel, FilmViewHolder>(FilmItemDiff()) {

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, false) }
        // Обрабатываем нажатие на весь элемент целиком и вызываем метод листенера,
        // который получаем из конструктора адаптера
        holder.binding.itemContainer.setOnClickListener {
            holder.binding.poster.transitionName = TRANSITION_NAME_FOR_IMAGE
            holder.binding.description.transitionName = TRANSITION_NAME_FOR_TEXT
            holder.binding.ratingDonut.transitionName = TRANSITION_NAME_FOR_RATING
            getItem(position)?.let { film ->
                clickListener.click(
                    film,
                    holder.binding.poster,
                    holder.binding.description,
                    holder.binding.ratingDonut
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(
            ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}