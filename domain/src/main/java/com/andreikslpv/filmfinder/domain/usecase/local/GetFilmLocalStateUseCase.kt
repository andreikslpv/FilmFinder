package com.andreikslpv.filmfinder.domain.usecase.local

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class GetFilmLocalStateUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(film: FilmDomainModel): FilmDomainModel {
        return filmsRepository.getFilmLocalState(film)
    }
}