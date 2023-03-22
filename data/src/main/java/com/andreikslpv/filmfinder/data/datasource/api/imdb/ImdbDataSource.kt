package com.andreikslpv.filmfinder.data.datasource.api.imdb

import android.content.Context
import androidx.paging.rxjava3.RxPagingSource
import com.andreikslpv.filmfinder.data.R
import com.andreikslpv.filmfinder.data.datasource.api.ApiCallback
import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.remote_module.imdb.ImdbServiceFilmsByCategory
import com.andreikslpv.filmfinder.remote_module.imdb.ImdbServiceSearchResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImdbDataSource @Inject constructor(
    private val context: Context,
    private val serviceCategory: ImdbServiceFilmsByCategory,
    private val serviceSearch: ImdbServiceSearchResult,
) : FilmsApiDataSource {

    private val categoryMap = mapOf(
        Pair(CategoryType.POPULAR, ImdbConstants.CATEGORY_POPULAR),
        Pair(CategoryType.TOP_250, ImdbConstants.CATEGORY_TOP_250),
        Pair(CategoryType.NOW_PLAYING, ImdbConstants.CATEGORY_NOW_PLAYING),
        Pair(CategoryType.UPCOMING, ImdbConstants.CATEGORY_UPCOMING),
        Pair(CategoryType.BOXOFFICE_WEEKEND, ImdbConstants.CATEGORY_BOXOFFICE_WEEKEND),
        Pair(CategoryType.BOXOFFICE_ALLTIME, ImdbConstants.CATEGORY_BOXOFFICE_ALLTIME),
    )

    override fun getFilmsByCategoryPagingSource(
        category: CategoryType,
        callback: ApiCallback
    ): RxPagingSource<Int, FilmDomainModel> {
        return ImdbPagingSourceFilmsByCategory(
            service = serviceCategory,
            language = context.getString(R.string.imdb_language),
            category = getPathFromCategory(category),
            callback = callback
        )
    }

    override fun getSearchResultPagingSource(query: String): RxPagingSource<Int, FilmDomainModel> {
        return ImdbPagingSourceSearchResult(
            service = serviceSearch,
            language = context.getString(R.string.imdb_language),
            query = query
        )
    }

    override fun getAvailableCategories(): List<CategoryType> {
        return categoryMap.keys.toList()
    }

    private fun getPathFromCategory(category: CategoryType): String {
        return categoryMap[category] ?: ""
    }

    override fun getApiType(): ValuesType {
        return ValuesType.IMDB
    }

    override fun checkComplianceApi(filmId: String): Boolean {
        return filmId.startsWith("tt",true)
    }

}