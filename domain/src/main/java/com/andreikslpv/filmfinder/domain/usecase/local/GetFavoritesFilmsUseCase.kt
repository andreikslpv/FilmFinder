package com.andreikslpv.filmfinder.domain.usecase.local

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import kotlinx.coroutines.flow.Flow

class GetFavoritesFilmsUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(): Flow<List<FilmDomainModel>> {
        return filmsRepository.getFavoritesFilms()
    }
}