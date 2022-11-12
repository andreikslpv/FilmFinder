package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class GetFavoritesFilmsUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(): List<FilmDomainModel> {
        return filmsRepository.getAllLocalSavedFilms().filter {
            it.isFavorite
        }
    }
}