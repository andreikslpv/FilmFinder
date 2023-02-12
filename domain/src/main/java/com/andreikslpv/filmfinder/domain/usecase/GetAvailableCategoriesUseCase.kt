package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.FilmsRepository
import kotlinx.coroutines.flow.Flow

class GetAvailableCategoriesUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(): Flow<List<CategoryType>> {
        return filmsRepository.getAvailableCategories()
    }
}