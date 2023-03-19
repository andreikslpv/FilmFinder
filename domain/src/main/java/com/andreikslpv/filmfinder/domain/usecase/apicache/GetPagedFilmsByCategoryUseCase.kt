package com.andreikslpv.filmfinder.domain.usecase.apicache

import androidx.paging.PagingData
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import io.reactivex.rxjava3.core.Flowable

class GetPagedFilmsByCategoryUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(category: CategoryType): Flowable<PagingData<FilmDomainModel>> {
        return filmsRepository.getPagedFilmsByCategory(category)
    }
}