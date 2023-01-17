package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType

class SaveFilmsToCacheUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(films: List<FilmDomainModel>, api: ValuesType, category: CategoryType) {
        filmsRepository.saveFilmsToCache(films, api, category)
    }
}