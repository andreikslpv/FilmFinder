package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class GetAllFilmsByPageUseCase(private val filmsRepository: FilmsRepository) {
    fun execute() : List<FilmDomainModel> {
        return filmsRepository.getAllFilmsByPage()
    }
}