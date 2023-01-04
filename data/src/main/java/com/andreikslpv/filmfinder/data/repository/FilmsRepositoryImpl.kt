package com.andreikslpv.filmfinder.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andreikslpv.filmfinder.data.datasource.api.tmdb.TmdbDataSourceFilmsByCategory
import com.andreikslpv.filmfinder.data.datasource.api.tmdb.TmdbDataSourceSearchFilm
import com.andreikslpv.filmfinder.data.datasource.api.tmdb.TmdbServiceFilmsByCategory
import com.andreikslpv.filmfinder.data.datasource.api.tmdb.TmdbServiceSearchFilm
import com.andreikslpv.filmfinder.data.datasource.api.tmdb.TmdbConstants
import com.andreikslpv.filmfinder.data.datasource.local.FilmsLocalDataSource
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class FilmsRepositoryImpl(
    private val localDataSource: FilmsLocalDataSource,
    private val language: String
) : FilmsRepository {

    private val retrofit: Retrofit
    private val pageSize = 10
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

    override fun getPagedFilmsByCategory(category: String): Flow<PagingData<FilmDomainModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                initialLoadSize = pageSize
            ),
            pagingSourceFactory = {
                TmdbDataSourceFilmsByCategory(
                    retrofit.create(TmdbServiceFilmsByCategory::class.java),
                    language,
                    category
                )
            }).flow
    }

    override fun getPagedSearchResult(query: String): Flow<PagingData<FilmDomainModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                initialLoadSize = pageSize
            ),
            pagingSourceFactory = {
                TmdbDataSourceSearchFilm(
                    retrofit.create(TmdbServiceSearchFilm::class.java),
                    language,
                    query
                )
            }).flow
    }

    override fun getAllLocalSavedFilms(): List<FilmDomainModel> {
        return LocalToDomainMapper.map(localDataSource.getItems())
    }

    override fun saveFilmToLocal(film: FilmDomainModel) {
        localDataSource.saveItem(DomainToLocalMapper.map(film))
    }
}