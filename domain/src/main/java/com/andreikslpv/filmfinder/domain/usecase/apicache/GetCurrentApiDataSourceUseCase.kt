package com.andreikslpv.filmfinder.domain.usecase.apicache

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.types.ValuesType
import kotlinx.coroutines.flow.MutableStateFlow

class GetCurrentApiDataSourceUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(): MutableStateFlow<ValuesType> {
        return filmsRepository.getCurrentApiDataSource()
    }
}