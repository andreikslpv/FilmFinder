package com.andreikslpv.filmfinder.repository

import com.andreikslpv.filmfinder.datasource.models.FilmsApiModel
import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel
import com.andreikslpv.filmfinder.domain.BaseMapper

object ApiToLocalMapper : BaseMapper<List<FilmsApiModel>, List<FilmsLocalModel>> {
    override fun map(type: List<FilmsApiModel>?): List<FilmsLocalModel> {
        return type?.map {
            FilmsLocalModel(
                id = it.id ?: -1,
                title = it.title ?: "",
                poster = it.poster ?: -1,
                description = it.description ?: "",
                descriptionFull = it.descriptionFull ?: "",
                isFavorite = false,
                isWatchLater = false
            )
        } ?: listOf()
    }
}
