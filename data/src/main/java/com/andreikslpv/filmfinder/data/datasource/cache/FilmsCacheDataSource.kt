package com.andreikslpv.filmfinder.data.datasource.cache

import androidx.paging.rxjava3.RxPagingSource
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType

interface FilmsCacheDataSource {

    fun putCategoryToCache(
        api: ValuesType,
        category: CategoryType,
        films: List<FilmDomainModel>,
        currentIndex: Int
    )

    fun deleteCache()

    fun getFilmsByCategoryPagingSource(
        api: ValuesType,
        category: CategoryType
    ): RxPagingSource<Int, FilmDomainModel>

    fun getSearchResultPagingSource(
        function: (string: String) -> Boolean,
        query: String
    ): RxPagingSource<Int, FilmDomainModel>
}