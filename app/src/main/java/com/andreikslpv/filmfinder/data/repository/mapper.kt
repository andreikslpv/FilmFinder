package com.andreikslpv.filmfinder.data.repository

import com.andreikslpv.filmfinder.data.datasource.models.FilmsApiModel
import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel
import com.andreikslpv.filmfinder.domain.BaseMapper

object ApiToLocalMapper : BaseMapper<List<FilmsApiModel>, List<FilmsLocalModel>> {
    override fun map(type: List<FilmsApiModel>?): List<FilmsLocalModel> {
        return type?.map {
            FilmsLocalModel(
                id = it.id ?: -1,
                title = it.title ?: "",
                poster = it.poster ?: -1,
                description = it.description ?: "",
                isFavorite = false,
                isWatchLater = false
            )
        } ?: listOf()
    }
}