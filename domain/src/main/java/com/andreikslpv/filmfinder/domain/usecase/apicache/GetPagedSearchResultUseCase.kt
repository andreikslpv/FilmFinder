package com.andreikslpv.filmfinder.domain.usecase.apicache

import androidx.paging.PagingData
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import io.reactivex.rxjava3.core.Flowable

class GetPagedSearchResultUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(query: String): Flowable<PagingData<FilmDomainModel>> {
        return filmsRepository.getPagedSearchResult(query)
    }
}