package com.andreikslpv.filmfinder.presentation.recyclers

import androidx.recyclerview.widget.DiffUtil
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class FilmDiff(private val oldList: List<FilmDomainModel>, private val newList: List<FilmDomainModel>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}