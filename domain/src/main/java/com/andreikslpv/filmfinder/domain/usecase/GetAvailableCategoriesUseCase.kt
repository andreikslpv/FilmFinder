package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.FilmsRepository
import kotlinx.coroutines.flow.MutableStateFlow

class GetAvailableCategoriesUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(): MutableStateFlow<List<CategoryType>> {
        return filmsRepository.getAvailableCategories()
    }
}