package com.andreikslpv.filmfinder.domain.usecase.apicache

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.types.ValuesType

class SetApiDataSourceUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(api: ValuesType) {
        when (api) {
            ValuesType.TMDB, ValuesType.IMDB -> {
                filmsRepository.setApiDataSource(api)
            }
            else -> {}
        }

    }
}