package com.andreikslpv.filmfinder.domain

import androidx.paging.PagingData
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import kotlinx.coroutines.flow.Flow

interface FilmsRepository {

    fun getPagedFilmsByCategory(category: CategoryType): Flow<PagingData<FilmDomainModel>>

    fun getPagedSearchResult(query: String): Flow<PagingData<FilmDomainModel>>

    fun getAvailableCategories(): List<CategoryType>

    fun getAllLocalSavedFilms(): List<FilmDomainModel>

    fun saveFilmToLocal(film: FilmDomainModel)

}