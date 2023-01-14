package com.andreikslpv.filmfinder.data.datasource.local

import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.BaseMapper

object LocalToDomainMapper : BaseMapper<List<FilmLocalModel>, List<FilmDomainModel>> {
    override fun map(type: List<FilmLocalModel>?): List<FilmDomainModel> {
        return type?.map {
            FilmDomainModel(
                id = it.id ?: "",
                title = it.title ?: "",
                posterPreview = it.posterPreview ?: "",
                posterDetails = it.posterDetails ?: "",
                description = it.description ?: "",
                rating = it.rating ?: 0.0,
                isFavorite = it.isFavorite ?: false,
                isWatchLater = it.isWatchLater ?: false
            )
        } ?: listOf()
    }
}

object DomainToLocalMapper : BaseMapper<FilmDomainModel, FilmLocalModel> {
    override fun map(type: FilmDomainModel?): FilmLocalModel {
        return FilmLocalModel(
            id = type?.id ?: "",
            title = type?.title ?: "",
            posterPreview = type?.posterPreview ?: "",
            posterDetails = type?.posterDetails ?: "",
            description = type?.description ?: "",
            rating = type?.rating ?: 0.0,
            isFavorite = type?.isFavorite ?: false,
            isWatchLater = type?.isWatchLater ?: false
        )
    }
}
