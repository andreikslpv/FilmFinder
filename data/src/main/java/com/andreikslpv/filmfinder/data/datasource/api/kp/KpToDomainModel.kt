package com.andreikslpv.filmfinder.data.datasource.api.kp

import com.andreikslpv.filmfinder.domain.BaseMapper
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

object KpToDomainModel : BaseMapper<List<KpDtoDoc>, List<FilmDomainModel>> {
    override fun map(type: List<KpDtoDoc>?): List<FilmDomainModel> {
        return type?.map {
            FilmDomainModel(
                id = it.id ?: -1,
                title = it.name ?: "",
                posterPreview = it.poster?.previewUrl ?: "",
                posterDetails = it.poster?.url ?: "",
                description = it.description ?: "",
                rating = it.rating?.kp ?: 0.0,
                isFavorite = false,
                isWatchLater = false
            )
        } ?: listOf()
    }
}