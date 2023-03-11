package com.andreikslpv.filmfinder.domain

import androidx.paging.PagingData
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface FilmsRepository {

    fun getPagedFilmsByCategory(category: CategoryType): Flow<PagingData<FilmDomainModel>>

    fun getPagedSearchResult(query: String): Flowable<PagingData<FilmDomainModel>>

    fun getAvailableCategories(): MutableStateFlow<List<CategoryType>>

    fun setApiDataSource(api: ValuesType)

    fun getCurrentApiDataSource(): MutableStateFlow<ValuesType>

    fun setCacheMode(mode: ValuesType)

    fun deleteAllCachedFilms()

    fun changeApiAvailability(newStatus: Boolean)

    fun getWatchLaterFilms(): Observable<List<FilmDomainModel>>

    fun getFavoritesFilms(): Observable<List<FilmDomainModel>>

    fun getFilmLocalState(filmId: String): Observable<FilmDomainModel>

    fun saveFilmToLocal(film: FilmDomainModel, replace: Boolean)


}