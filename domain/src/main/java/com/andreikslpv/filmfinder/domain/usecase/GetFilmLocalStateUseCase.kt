package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class GetFilmLocalStateUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(film: FilmDomainModel): FilmDomainModel {
        val localFilms = filmsRepository.getAllLocalSavedFilms()
        var localFilm: FilmDomainModel? = null

        if (localFilms.any { it.id == film.id })
            localFilm = localFilms.filter { it.id == film.id }[0]

        if (localFilm != null) {
            film.isFavorite = localFilm.isFavorite
            film.isWatchLater = localFilm.isWatchLater
        }
        return film
    }
}