package com.andreikslpv.filmfinder

import android.app.Application
import com.andreikslpv.filmfinder.data.datasource.local.FilmsJsonDataSource
import com.andreikslpv.filmfinder.data.repository.FilmsRepositoryImpl
import com.andreikslpv.filmfinder.domain.usecase.*
import timber.log.Timber
import java.io.File

const val NAME_OF_LOCAL_STORAGE = "local.json"

class App : Application() {
    private lateinit var filmsRepository: FilmsRepositoryImpl
    lateinit var changeFilmLocalStateUseCase: ChangeFilmLocalStateUseCase
    lateinit var getFavoritesFilmsUseCase: GetFavoritesFilmsUseCase
    lateinit var getFilmLocalStateUseCase: GetFilmLocalStateUseCase
    lateinit var getPagedSearchResultUseCase: GetPagedSearchResultUseCase
    lateinit var getSearchResultUseCase: GetSearchResultUseCase
    lateinit var getWatchLaterFilmsUseCase: GetWatchLaterFilmsUseCase
    lateinit var getPagedFilmsByCategoryUseCase: GetPagedFilmsByCategoryUseCase

    override fun onCreate() {
        super.onCreate()

        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        //Инициализируем репозиторий
        filmsRepository = FilmsRepositoryImpl(
            FilmsJsonDataSource(File("${filesDir}/$NAME_OF_LOCAL_STORAGE")),
            getString(R.string.tmdb_language)
        )
        //Инициализируем usecase
        changeFilmLocalStateUseCase = ChangeFilmLocalStateUseCase(filmsRepository)
        getFavoritesFilmsUseCase = GetFavoritesFilmsUseCase(filmsRepository)
        getFilmLocalStateUseCase = GetFilmLocalStateUseCase(filmsRepository)
        getPagedSearchResultUseCase = GetPagedSearchResultUseCase(filmsRepository)
        getSearchResultUseCase = GetSearchResultUseCase()
        getWatchLaterFilmsUseCase = GetWatchLaterFilmsUseCase(filmsRepository)
        getPagedFilmsByCategoryUseCase = GetPagedFilmsByCategoryUseCase(filmsRepository)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }
}