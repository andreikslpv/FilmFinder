package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.types.ValuesType

class GetCurrentApiDataSourceUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(): ValuesType {
        return filmsRepository.getCurrentApiDataSource()
    }
}