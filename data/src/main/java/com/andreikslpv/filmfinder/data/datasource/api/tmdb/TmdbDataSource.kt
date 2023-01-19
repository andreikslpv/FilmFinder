package com.andreikslpv.filmfinder.data.datasource.api.tmdb

import android.content.Context
import androidx.paging.PagingSource
import com.andreikslpv.filmfinder.data.R
import com.andreikslpv.filmfinder.data.datasource.api.ApiCallback
import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbDataSource @Inject constructor(
    private val context: Context,
    okHttpClient: OkHttpClient,
) : FilmsApiDataSource {

    private val categoryMap = mapOf(
        Pair(CategoryType.POPULAR, TmdbConstants.CATEGORY_POPULAR),
        Pair(CategoryType.TOP_RATED, TmdbConstants.CATEGORY_TOP_RATED),
        Pair(CategoryType.NOW_PLAYING, TmdbConstants.CATEGORY_NOW_PLAYING),
        Pair(CategoryType.UPCOMING, TmdbConstants.CATEGORY_UPCOMING),
    )
    private val retrofit: Retrofit = Retrofit.Builder()
        //Указываем базовый URL из констант
        .baseUrl(TmdbConstants.BASE_URL)
        //Добавляем конвертер
        .addConverterFactory(GsonConverterFactory.create())
        //Добавляем кастомный клиент
        .client(okHttpClient)
        .build()

    override fun getFilmsByCategoryPagingSource(
        category: CategoryType,
        callback: ApiCallback
    ): PagingSource<Int, FilmDomainModel> {
        return TmdbPagingSourceFilmsByCategory(
            retrofit.create(TmdbServiceFilmsByCategory::class.java),
            context.getString(R.string.tmdb_language),
            getPathFromCategory(category),
            callback,
        )
    }

    override fun getSearchResultPagingSource(query: String): PagingSource<Int, FilmDomainModel> {
        return TmdbPagingSourceSearchResult(
            retrofit.create(TmdbServiceSearchResult::class.java),
            context.getString(R.string.tmdb_language),
            query
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

}