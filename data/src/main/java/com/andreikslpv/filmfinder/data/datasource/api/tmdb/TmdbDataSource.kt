package com.andreikslpv.filmfinder.data.datasource.api.tmdb

import android.content.Context
import androidx.paging.PagingSource
import com.andreikslpv.filmfinder.data.R
import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.domain.CategoryType
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbDataSource @Inject constructor(
    private val context: Context,
    private val retrofit: Retrofit
) : FilmsApiDataSource {

    override fun getFilmsByCategoryPagingSource(category: String): PagingSource<Int, FilmDomainModel> {
        return TmdbPagingSourceFilmsByCategory(
            retrofit.create(TmdbServiceFilmsByCategory::class.java),
            context.getString(R.string.tmdb_language),
            category
        )
    }

    override fun getSearchResultPagingSource(query: String): PagingSource<Int, FilmDomainModel> {
        return TmdbPagingSourceSearchResult(
            retrofit.create(TmdbServiceSearchResult::class.java),
            context.getString(R.string.tmdb_language),
            query
        )
    }

    override fun getAvailableCategories(): Map<CategoryType, String> {
        return mapOf(
            Pair(CategoryType.POPULAR, TmdbConstants.CATEGORY_POPULAR),
            Pair(CategoryType.TOP_RATED, TmdbConstants.CATEGORY_TOP_RATED),
            Pair(CategoryType.NOW_PLAYING, TmdbConstants.CATEGORY_NOW_PLAYING),
            Pair(CategoryType.UPCOMING, TmdbConstants.CATEGORY_UPCOMING),
        )
    }
}