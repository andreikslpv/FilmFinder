package com.andreikslpv.filmfinder.domain

import androidx.paging.PagingData
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface FilmsRepository {

    fun getPagedFilmsByCategory(category: CategoryType): Flow<PagingData<FilmDomainModel>>

    fun getPagedSearchResult(query: String): Flow<PagingData<FilmDomainModel>>

    fun getAvailableCategories(): MutableStateFlow<List<CategoryType>>

    fun setApiDataSource(api: ValuesType)

    fun getCurrentApiDataSource(): MutableStateFlow<ValuesType>

    fun setCacheMode(mode: ValuesType)

    fun deleteAllCachedFilms()

    fun changeApiAvailability(newStatus: Boolean)

    fun getWatchLaterFilms(): Flow<List<FilmDomainModel>>

    fun getFavoritesFilms(): Flow<List<FilmDomainModel>>

    fun getFilmLocalState(filmId: String): Flow<FilmDomainModel>

    fun saveFilmToLocal(film: FilmDomainModel)


}