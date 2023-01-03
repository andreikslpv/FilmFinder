package com.andreikslpv.filmfinder

import android.app.Application
import com.andreikslpv.filmfinder.data.datasource.api.services.TmdbCategoryService
import com.andreikslpv.filmfinder.data.datasource.api.tmdb.FilmsTmdbDataSource
import com.andreikslpv.filmfinder.data.datasource.api.utils.TmdbConstants
import com.andreikslpv.filmfinder.data.datasource.local.FilmsJsonDataSource
import com.andreikslpv.filmfinder.data.repository.FilmsRepositoryImpl
import com.andreikslpv.filmfinder.domain.usecase.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

const val NAME_OF_LOCAL_STORAGE = "local.json"

class App : Application() {
    private lateinit var filmsRepository: FilmsRepositoryImpl
    lateinit var changeFilmLocalStateUseCase: ChangeFilmLocalStateUseCase
    lateinit var getFavoritesFilmsUseCase: GetFavoritesFilmsUseCase
    lateinit var getFilmLocalStateUseCase: GetFilmLocalStateUseCase
    lateinit var getFilmsFromApiUseCase: GetFilmsFromApiUseCase
    lateinit var getSearchResultFromApiUseCase: GetSearchResultFromApiUseCase
    lateinit var getSearchResultUseCase: GetSearchResultUseCase
    lateinit var getWatchLaterFilmsUseCase: GetWatchLaterFilmsUseCase
    lateinit var getPagedFilmsByCategoryUseCase: GetPagedFilmsByCategoryUseCase

    override fun onCreate() {
        super.onCreate()

        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        //Инициализируем репозиторий
        filmsRepository = FilmsRepositoryImpl(
            FilmsTmdbDataSource(),
            FilmsJsonDataSource(
                File("${filesDir}/$NAME_OF_LOCAL_STORAGE")
            ),
            provideTmdbCategoryService()
        )
        //Инициализируем usecase
        changeFilmLocalStateUseCase = ChangeFilmLocalStateUseCase(filmsRepository)
        getFavoritesFilmsUseCase = GetFavoritesFilmsUseCase(filmsRepository)
        getFilmLocalStateUseCase = GetFilmLocalStateUseCase(filmsRepository)
        getFilmsFromApiUseCase = GetFilmsFromApiUseCase(filmsRepository)
        getSearchResultFromApiUseCase = GetSearchResultFromApiUseCase(filmsRepository)
        getSearchResultUseCase = GetSearchResultUseCase()
        getWatchLaterFilmsUseCase = GetWatchLaterFilmsUseCase(filmsRepository)
        getPagedFilmsByCategoryUseCase = GetPagedFilmsByCategoryUseCase(filmsRepository)


        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun provideTmdbCategoryService(): TmdbCategoryService {
        //Создаём кастомный клиент
        val okHttpClient = OkHttpClient.Builder()
            //Настраиваем таймауты для медленного интернета
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            //Добавляем логгер
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (com.andreikslpv.filmfinder.data.BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()
        //Создаем Ретрофит
        val retrofit = Retrofit.Builder()
            //Указываем базовый URL из констант
            .baseUrl(TmdbConstants.BASE_URL)
            //Добавляем конвертер
            .addConverterFactory(GsonConverterFactory.create())
            //Добавляем кастомный клиент
            .client(okHttpClient)
            .build()
        //Создаем сам сервис с методами для запросов
        return retrofit.create(TmdbCategoryService::class.java)
    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }
}