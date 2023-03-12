package com.andreikslpv.filmfinder.domain.usecase.apicache

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.types.ValuesType
import io.reactivex.rxjava3.core.Observable

class GetCurrentApiDataSourceUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(): Observable<ValuesType> {
        return filmsRepository.getCurrentApiDataSource()
    }
}