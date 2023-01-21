package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.FilmsRepository

class ChangeNetworkAvailabilityUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(newStatus: Boolean) {
        filmsRepository.changeNetworkAvailability(newStatus)
    }
}