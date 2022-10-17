package com.andreikslpv.filmfinder.presentation.recyclers

import androidx.recyclerview.widget.DiffUtil
import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel

class FilmDiff(private val oldList: List<FilmsLocalModel>, private val newList: List<FilmsLocalModel>): DiffUtil.Callback() {
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