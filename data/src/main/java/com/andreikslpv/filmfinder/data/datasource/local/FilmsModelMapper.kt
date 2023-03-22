package com.andreikslpv.filmfinder.data.datasource.local

import com.andreikslpv.filmfinder.database_module.models.CategoryAndFilmModel
import com.andreikslpv.filmfinder.database_module.models.CategoryModel
import com.andreikslpv.filmfinder.database_module.models.FilmLocalModel
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.BaseMapper

object LocalToDomainListMapper : BaseMapper<List<FilmLocalModel>, List<FilmDomainModel>> {
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

object DomainToLocalListMapper : BaseMapper<List<FilmDomainModel>, List<FilmLocalModel>> {
    override fun map(type: List<FilmDomainModel>?): List<FilmLocalModel> {
        return type?.map {
            FilmLocalModel(
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
            isWatchLater = type?.isWatchLater ?: false
        )
    }
}

object CategoryAndFilmToDomainMapper :
    BaseMapper<List<CategoryAndFilmModel>, List<FilmDomainModel>> {
    override fun map(type: List<CategoryAndFilmModel>?): List<FilmDomainModel> {
        return type?.map {
            FilmDomainModel(
                id = it.film.id ?: "",
                title = it.film.title ?: "",
                posterPreview = it.film.posterPreview ?: "",
                posterDetails = it.film.posterDetails ?: "",
                description = it.film.description ?: "",
                rating = it.film.rating ?: 0.0,
                isFavorite = it.film.isFavorite ?: false,
                isWatchLater = it.film.isWatchLater ?: false
            )
        } ?: listOf()
    }
}