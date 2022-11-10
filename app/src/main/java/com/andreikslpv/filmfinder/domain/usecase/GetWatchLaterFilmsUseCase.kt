package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel

class GetWatchLaterFilmsUseCase(private val filmsRepository: FilmsRepository) {
    fun execute() : List<FilmsLocalModel> {
        return filmsRepository.getAllLocalSavedFilms().filter {
            it.isWatchLater
        }
    }
}