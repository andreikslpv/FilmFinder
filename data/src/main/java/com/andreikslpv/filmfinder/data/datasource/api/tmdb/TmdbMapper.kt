package com.andreikslpv.filmfinder.data.datasource.api.tmdb

import com.andreikslpv.filmfinder.domain.BaseMapper
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

object TmdbToDomainModel : BaseMapper<List<TmdbDtoFilm>, List<FilmDomainModel>> {
    override fun map(type: List<TmdbDtoFilm>?): List<FilmDomainModel> {
        return type?.map {
            FilmDomainModel(
                id = it.id.toString() ?: "",
                title = it.title ?: "",
                posterPreview = (TmdbConstants.IMAGES_URL + "w342" + it.posterPath) ?: "",
                posterDetails = (TmdbConstants.IMAGES_URL + "w780" + it.posterPath) ?: "",
                description = it.overview ?: "",
                rating = it.voteAverage ?: 0.0,
                isFavorite = false,
                isWatchLater = false
            )
        } ?: listOf()
    }
}