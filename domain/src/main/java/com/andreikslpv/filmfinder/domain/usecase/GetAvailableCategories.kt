package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.CategoryType
import com.andreikslpv.filmfinder.domain.FilmsRepository

class GetAvailableCategories(private val filmsRepository: FilmsRepository) {
    fun execute(): List<CategoryType> {
        return filmsRepository.getAvailableCategories()
    }
}