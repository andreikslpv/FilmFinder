package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.ApiCallback
import com.andreikslpv.filmfinder.domain.FilmsRepository

class GetSearchResultFromApiUseCase(private val filmsRepository: FilmsRepository) {

    fun execute(query: String, page: Int, language: String, callback: ApiCallback) {
        filmsRepository.getSearchResultFromApi(query, page, language, callback)
    }
}