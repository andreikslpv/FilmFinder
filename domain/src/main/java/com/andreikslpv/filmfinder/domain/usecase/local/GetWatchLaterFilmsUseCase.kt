package com.andreikslpv.filmfinder.domain.usecase.local

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import kotlinx.coroutines.flow.Flow

class GetWatchLaterFilmsUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(): Flow<List<FilmDomainModel>> {
        return filmsRepository.getWatchLaterFilms()
    }
}