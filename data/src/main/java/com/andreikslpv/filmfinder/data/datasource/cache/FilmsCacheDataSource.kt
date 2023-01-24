package com.andreikslpv.filmfinder.data.datasource.cache

import androidx.paging.PagingSource
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType

interface FilmsCacheDataSource {

    fun putCategoryToCache(api: ValuesType, category: CategoryType, films: List<FilmDomainModel>)

    fun deleteCache()

    fun getFilmsByCategoryPagingSource(
        api: ValuesType,
        category: CategoryType
    ): PagingSource<Int, FilmDomainModel>

    fun getSearchResultPagingSource(
        api: ValuesType,
        query: String
    ): PagingSource<Int, FilmDomainModel>
}