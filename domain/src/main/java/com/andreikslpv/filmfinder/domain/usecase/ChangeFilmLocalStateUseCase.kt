package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class ChangeFilmLocalStateUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(film: FilmDomainModel) {
        filmsRepository.saveFilmToLocal(film)
    }
}