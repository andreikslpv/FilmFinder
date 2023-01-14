package com.andreikslpv.filmfinder.data.datasource.api

import androidx.paging.PagingSource
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.ValuesType

interface FilmsApiDataSource {

    fun getFilmsByCategoryPagingSource(category: CategoryType): PagingSource<Int, FilmDomainModel>

    fun getSearchResultPagingSource(query: String): PagingSource<Int, FilmDomainModel>

    fun getAvailableCategories(): List<CategoryType>

    fun getPathFromCategory(category: CategoryType): String

    fun getApiType(): ValuesType
}