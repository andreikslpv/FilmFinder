package com.andreikslpv.filmfinder.data.datasource.api.tmdb

import android.content.Context
import androidx.paging.rxjava3.RxPagingSource
import com.andreikslpv.filmfinder.data.R
import com.andreikslpv.filmfinder.data.datasource.api.ApiCallback
import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbServiceFilmsByCategory
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbServiceSearchResult
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbDataSource @Inject constructor(
    private val context: Context,
    private val serviceCategory: TmdbServiceFilmsByCategory,
    private val serviceSearch: TmdbServiceSearchResult,
) : FilmsApiDataSource {

    private val categoryMap = mapOf(
        Pair(CategoryType.POPULAR, TmdbConstants.CATEGORY_POPULAR),
        Pair(CategoryType.TOP_RATED, TmdbConstants.CATEGORY_TOP_RATED),
        Pair(CategoryType.NOW_PLAYING, TmdbConstants.CATEGORY_NOW_PLAYING),
        Pair(CategoryType.UPCOMING, TmdbConstants.CATEGORY_UPCOMING),
    )

    override fun getFilmsByCategoryPagingSource(
        category: CategoryType,
        callback: ApiCallback
    ): RxPagingSource<Int, FilmDomainModel> {
        return TmdbPagingSourceFilmsByCategory(
            service = serviceCategory,
            language = context.getString(R.string.tmdb_language),
            category = getPathFromCategory(category),
            callback = callback,
        )
    }

    override fun getSearchResultPagingSource(query: String): RxPagingSource<Int, FilmDomainModel> {
        return TmdbPagingSourceSearchResult(
            service = serviceSearch,
            language = context.getString(R.string.tmdb_language),
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
        return ValuesType.TMDB
    }

    override fun checkComplianceApi(filmId: String): Boolean {
        return filmId.toDoubleOrNull() != null
    }

    override fun getFilmByIdFromApi(filmId: String): Single<List<FilmDomainModel>> {
        TODO("Not yet implemented")
    }

}