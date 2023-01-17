package com.andreikslpv.filmfinder.domain

import androidx.paging.PagingData
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import kotlinx.coroutines.flow.Flow

interface FilmsRepository {

    fun getPagedFilmsByCategory(category: CategoryType): Flow<PagingData<FilmDomainModel>>

    fun getPagedSearchResult(query: String): Flow<PagingData<FilmDomainModel>>

    fun getAvailableCategories(): List<CategoryType>

    fun setApiDataSource(api: ValuesType)

    fun getCurrentApiDataSource(): ValuesType

    fun saveFilmsToCache(films: List<FilmDomainModel>, api: ValuesType, category: CategoryType)

    fun deleteAllCachedFilms()

    fun changeNetworkAvailability(newStatus: Boolean)

    fun getAllLocalSavedFilms(): List<FilmDomainModel>

    fun saveFilmToLocal(film: FilmDomainModel)


}