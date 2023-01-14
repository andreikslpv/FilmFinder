package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.FilmsRepository

class GetAvailableCategoriesUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(): List<CategoryType> {
        return filmsRepository.getAvailableCategories()
    }
}