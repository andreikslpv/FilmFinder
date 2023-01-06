package com.andreikslpv.filmfinder.domain.usecase

import androidx.paging.PagingData
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import kotlinx.coroutines.flow.Flow

class GetPagedFilmsByCategoryUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(category: String): Flow<PagingData<FilmDomainModel>> {
        return filmsRepository.getPagedFilmsByCategory(category)
    }
}