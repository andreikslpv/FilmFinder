package com.andreikslpv.filmfinder.data.datasource.api.tmdb

import androidx.paging.PagingSource
import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.domain.CategoryType
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TmdbDataSource(private val language: String) : FilmsApiDataSource {
    private val retrofit: Retrofit
    private val timeout = 30L

    init {
        //Создаём кастомный клиент
        val okHttpClient = OkHttpClient.Builder()
            //Настраиваем таймауты для медленного интернета
            .callTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            //Добавляем логгер
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (com.andreikslpv.filmfinder.data.BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()
        //Создаем Ретрофит
        retrofit = Retrofit.Builder()
            //Указываем базовый URL из констант
            .baseUrl(TmdbConstants.BASE_URL)
            //Добавляем конвертер
            .addConverterFactory(GsonConverterFactory.create())
            //Добавляем кастомный клиент
            .client(okHttpClient)
            .build()
    }

    override fun getFilmsByCategoryPagingSource(category: String): PagingSource<Int, FilmDomainModel> {
        return TmdbPagingSourceFilmsByCategory(
            retrofit.create(TmdbServiceFilmsByCategory::class.java),
            language,
            category
        )
    }

    override fun getSearchResultPagingSource(query: String): PagingSource<Int, FilmDomainModel> {
        return TmdbPagingSourceSearchResult(
            retrofit.create(TmdbServiceSearchResult::class.java),
            language,
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