package com.andreikslpv.filmfinder.presentation.ui.recyclers

import androidx.recyclerview.widget.DiffUtil
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class FilmItemDiff : DiffUtil.ItemCallback<FilmDomainModel>() {
    override fun areItemsTheSame(oldItem: FilmDomainModel, newItem: FilmDomainModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FilmDomainModel, newItem: FilmDomainModel): Boolean {
        return oldItem == newItem
    }
}