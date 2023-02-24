package com.andreikslpv.filmfinder.domain.usecase.apicache

import androidx.paging.PagingData
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import kotlinx.coroutines.flow.Flow

class GetPagedSearchResultUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(query: String): Flow<PagingData<FilmDomainModel>> {
        return filmsRepository.getPagedSearchResult(query)
    }
}