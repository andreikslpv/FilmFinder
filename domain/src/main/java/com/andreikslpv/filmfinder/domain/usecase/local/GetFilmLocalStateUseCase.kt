package com.andreikslpv.filmfinder.domain.usecase.local

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import kotlinx.coroutines.flow.Flow

class GetFilmLocalStateUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(filmId: String): Flow<FilmDomainModel> {
        return filmsRepository.getFilmLocalState(filmId)
    }
}