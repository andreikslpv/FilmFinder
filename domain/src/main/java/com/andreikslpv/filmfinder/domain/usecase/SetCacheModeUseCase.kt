package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.types.ValuesType

class SetCacheModeUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(mode: ValuesType) {
        filmsRepository.setCacheMode(mode)
    }
}