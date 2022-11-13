package com.andreikslpv.filmfinder.data.repository

import com.andreikslpv.filmfinder.data.datasource.api.models.FilmApiModel
import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.BaseMapper

object ApiToDomainMapper : BaseMapper<List<FilmApiModel>, List<FilmDomainModel>> {
    override fun map(type: List<FilmApiModel>?): List<FilmDomainModel> {
        return type?.map {
            FilmDomainModel(
                id = it.id ?: -1,
                title = it.title ?: "",
                poster = it.poster ?: -1,
                description = it.description ?: "",
                rating = it.rating ?: 0f,
                isFavorite = false,
                isWatchLater = false
            )
        } ?: listOf()
    }
}

object LocalToDomainMapper : BaseMapper<List<FilmLocalModel>, List<FilmDomainModel>> {
    override fun map(type: List<FilmLocalModel>?): List<FilmDomainModel> {
        return type?.map {
            FilmDomainModel(
                id = it.id ?: -1,
                title = it.title ?: "",
                poster = it.poster ?: -1,
                description = it.description ?: "",
                rating = it.rating ?: 0f,
                isFavorite = it.isFavorite ?: false,
                isWatchLater = it.isWatchLater ?: false
            )
        } ?: listOf()
    }
}

object DomainToLocalMapper : BaseMapper<FilmDomainModel, FilmLocalModel> {
    override fun map(type: FilmDomainModel?): FilmLocalModel {
        return FilmLocalModel(
            id = type?.id ?: -1,
            title = type?.title ?: "",
            poster = type?.poster ?: -1,
            description = type?.description ?: "",
            rating = type?.rating ?: 0f,
            isFavorite = type?.isFavorite ?: false,
            isWatchLater = type?.isWatchLater ?: false
        )
    }
}
