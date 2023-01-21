package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.FilmsRepository

class DeleteAllCachedFilmsUseCase(private val filmsRepository: FilmsRepository) {
    fun execute() {
        filmsRepository.deleteAllCachedFilms()
    }
}