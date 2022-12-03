package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andreikslpv.filmfinder.data.datasource.api.FilmsTestDataSource
import com.andreikslpv.filmfinder.data.datasource.cache.FilmsCacheDataSource
import com.andreikslpv.filmfinder.data.datasource.local.FilmsJsonDataSource
import com.andreikslpv.filmfinder.data.repository.FilmsRepositoryImpl
import com.andreikslpv.filmfinder.domain.usecase.ChangeFilmLocalStateUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetAllFilmsByPageUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetFilmLocalStateUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetSearchResultUseCase
import com.andreikslpv.filmfinder.presentation.MainActivity
import java.io.File

class MainViewModelFactory(pathName: String): ViewModelProvider.Factory {


    private val filmsRepository by lazy {
        FilmsRepositoryImpl(
            FilmsCacheDataSource(),
            FilmsTestDataSource(),
            FilmsJsonDataSource(
                File(pathName)
            )
        )
    }

    private val getAllFilmsByPageUseCase by lazy {
        GetAllFilmsByPageUseCase(filmsRepository)
    }

    private val getFilmLocalStateUseCase by lazy {
        GetFilmLocalStateUseCase(filmsRepository)
    }

    private val changeFilmLocalStateUseCase by lazy {
        ChangeFilmLocalStateUseCase(filmsRepository)
    }

    private val getSearchResultUseCase by lazy { GetSearchResultUseCase() }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            getAllFilmsByPageUseCase,
            getFilmLocalStateUseCase,
            changeFilmLocalStateUseCase,
            getSearchResultUseCase
        ) as T
    }

}