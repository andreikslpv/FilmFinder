package com.andreikslpv.filmfinder.data.datasource.api

import androidx.paging.PagingSource
import com.andreikslpv.filmfinder.domain.CategoryType
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

interface FilmsApiDataSource {

    fun getFilmsByCategoryPagingSource(category: CategoryType): PagingSource<Int, FilmDomainModel>

    fun getSearchResultPagingSource(query: String): PagingSource<Int, FilmDomainModel>

    fun getAvailableCategories(): List<CategoryType>

    fun getPathFromCategory(category: CategoryType): String
}