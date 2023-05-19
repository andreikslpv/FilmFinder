package com.andreikslpv.filmfinder.data.datasource.api

import androidx.paging.rxjava3.RxPagingSource
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import io.reactivex.rxjava3.core.Single

interface FilmsApiDataSource {

    fun getFilmsByCategoryPagingSource(category: CategoryType, callback: ApiCallback): RxPagingSource<Int, FilmDomainModel>

    fun getSearchResultPagingSource(query: String): RxPagingSource<Int, FilmDomainModel>

    fun getAvailableCategories(): List<CategoryType>

    fun getApiType(): ValuesType

    fun checkComplianceApi(filmId: String): Boolean

    fun getFilmByIdFromApi(filmId: String): Single<List<FilmDomainModel>>

}