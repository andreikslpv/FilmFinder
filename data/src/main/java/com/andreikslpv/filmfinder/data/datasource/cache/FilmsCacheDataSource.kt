package com.andreikslpv.filmfinder.data.datasource.cache

import androidx.paging.PagingSource
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType

interface FilmsCacheDataSource {

    fun saveFilmsToCache(films: List<FilmDomainModel>, api: ValuesType, category: CategoryType)

    fun deleteCachedFilms(api: ValuesType, category: CategoryType)

    fun deleteAllCachedFilms()

    fun getFilmsByCategoryPagingSource(
        api: ValuesType,
        category: CategoryType
    ): PagingSource<Int, FilmDomainModel>

    fun getSearchResultPagingSource(
        api: ValuesType,
        query: String
    ): PagingSource<Int, FilmDomainModel>
}