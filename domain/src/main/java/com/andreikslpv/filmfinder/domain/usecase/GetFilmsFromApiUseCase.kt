package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.ApiCallback
import com.andreikslpv.filmfinder.domain.FilmsRepository

class GetFilmsFromApiUseCase(private val filmsRepository: FilmsRepository) {

    fun execute(page: Int, language: String, callback: ApiCallback) {
        filmsRepository.getFilmsFromApi(page, language, callback)
    }
}