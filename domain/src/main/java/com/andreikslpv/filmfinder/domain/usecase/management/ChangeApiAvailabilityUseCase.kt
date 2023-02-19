package com.andreikslpv.filmfinder.domain.usecase.management

import com.andreikslpv.filmfinder.domain.FilmsRepository

class ChangeApiAvailabilityUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(newStatus: Boolean) {
        filmsRepository.changeApiAvailability(newStatus)
    }
}