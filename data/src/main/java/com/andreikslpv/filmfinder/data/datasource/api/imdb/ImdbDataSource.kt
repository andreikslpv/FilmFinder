package com.andreikslpv.filmfinder.data.datasource.api.imdb

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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class ImdbDataSource @Inject constructor(
    private val context: Context,
    okHttpClient: OkHttpClient,
) : FilmsApiDataSource {

    private val categoryMap = mapOf(
        Pair(CategoryType.POPULAR, ImdbConstants.CATEGORY_POPULAR),
        Pair(CategoryType.TOP_250, ImdbConstants.CATEGORY_TOP_250),
        Pair(CategoryType.NOW_PLAYING, ImdbConstants.CATEGORY_NOW_PLAYING),
        Pair(CategoryType.UPCOMING, ImdbConstants.CATEGORY_UPCOMING),
        Pair(CategoryType.BOXOFFICE_WEEKEND, ImdbConstants.CATEGORY_BOXOFFICE_WEEKEND),
        Pair(CategoryType.BOXOFFICE_ALLTIME, ImdbConstants.CATEGORY_BOXOFFICE_ALLTIME),
    )

    private val retrofit: Retrofit = Retrofit.Builder()
        //Указываем базовый URL из констант
        .baseUrl(ImdbConstants.BASE_URL)
        //Добавляем конвертер
        .addConverterFactory(GsonConverterFactory.create())
        //Добавляем кастомный клиент
        .client(okHttpClient)
        .build()

    override fun getFilmsByCategoryPagingSource(
        category: CategoryType,
        callback: ApiCallback
    ): PagingSource<Int, FilmDomainModel> {
        return ImdbPagingSourceFilmsByCategory(
            retrofit.create(ImdbServiceFilmsByCategory::class.java),
            context.getString(R.string.imdb_language),
            getPathFromCategory(category),
            callback
        )
    }

    override fun getSearchResultPagingSource(query: String): PagingSource<Int, FilmDomainModel> {
        return ImdbPagingSourceSearchResult(
            retrofit.create(ImdbServiceSearchResult::class.java),
            context.getString(R.string.imdb_language),
            query
        )
    }

    override suspend fun getAvailableCategories(): List<CategoryType> {
        return suspendCoroutine {
            it.resume(categoryMap.keys.toList())
        }

//        return categoryMap.keys.toList()
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