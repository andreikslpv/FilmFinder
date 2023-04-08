package com.andreikslpv.filmfinder.data.datasource.local

import com.andreikslpv.filmfinder.database_module.models.CategoryAndFilmModel
import com.andreikslpv.filmfinder.database_module.models.FilmLocalModel
import com.andreikslpv.filmfinder.domain.BaseMapper
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

object LocalToDomainListMapper : BaseMapper<List<FilmLocalModel>, List<FilmDomainModel>> {
    override fun map(type: List<FilmLocalModel>?): List<FilmDomainModel> {
        return type?.map {
            FilmDomainModel(
                id = it.id,
                title = it.title,
                posterPreview = it.posterPreview,
                posterDetails = it.posterDetails,
                description = it.description,
                rating = it.rating,
                isFavorite = it.isFavorite,
                isWatchLater = it.isWatchLater,
                reminderTime = it.reminderTime,
            )
        } ?: listOf()
    }
}

object DomainToLocalListMapper : BaseMapper<List<FilmDomainModel>, List<FilmLocalModel>> {
    override fun map(type: List<FilmDomainModel>?): List<FilmLocalModel> {
        return type?.map {
            FilmLocalModel(
                id = it.id,
                title = it.title,
                posterPreview = it.posterPreview,
                posterDetails = it.posterDetails,
                description = it.description,
                rating = it.rating,
                isFavorite = it.isFavorite,
                isWatchLater = it.isWatchLater,
                reminderTime = it.reminderTime,
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
            isWatchLater = type?.isWatchLater ?: false,
            reminderTime = type?.reminderTime ?: 0L,
        )
    }
}

object LocalToDomainMapper : BaseMapper<FilmLocalModel, FilmDomainModel> {
    override fun map(type: FilmLocalModel?): FilmDomainModel {
        return FilmDomainModel(
            id = type?.id ?: "",
            title = type?.title ?: "",
            posterPreview = type?.posterPreview ?: "",
            posterDetails = type?.posterDetails ?: "",
            description = type?.description ?: "",
            rating = type?.rating ?: 0.0,
            isFavorite = type?.isFavorite ?: false,
            isWatchLater = type?.isWatchLater ?: false,
            reminderTime = type?.reminderTime ?: 0L,
        )
    }
}

object CategoryAndFilmToDomainMapper :
    BaseMapper<List<CategoryAndFilmModel>, List<FilmDomainModel>> {
    override fun map(type: List<CategoryAndFilmModel>?): List<FilmDomainModel> {
        return type?.map {
            FilmDomainModel(
                id = it.film.id,
                title = it.film.title,
                posterPreview = it.film.posterPreview,
                posterDetails = it.film.posterDetails,
                description = it.film.description,
                rating = it.film.rating,
                isFavorite = it.film.isFavorite,
                isWatchLater = it.film.isWatchLater,
                reminderTime = it.film.reminderTime,
            )
        } ?: listOf()
    }
}