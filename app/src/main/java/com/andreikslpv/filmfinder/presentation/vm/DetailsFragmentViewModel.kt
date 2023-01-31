package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.local.ChangeFilmLocalStateUseCase
import com.andreikslpv.filmfinder.domain.usecase.local.GetFilmLocalStateUseCase
import java.util.concurrent.Executors
import javax.inject.Inject

class DetailsFragmentViewModel : ViewModel() {
    val filmLiveData: MutableLiveData<FilmDomainModel> = MutableLiveData()

    @Inject
    lateinit var changeFilmLocalStateUseCase: ChangeFilmLocalStateUseCase
    @Inject
    lateinit var getFilmLocalStateUseCase: GetFilmLocalStateUseCase

    init {
        App.instance.dagger.inject(this)
    }

    fun setFilm(filmId: String) {
        Executors.newSingleThreadExecutor().execute {
            filmLiveData.postValue(getFilmLocalStateUseCase.execute(filmId))
        }
    }

    fun changeWatchLaterField() {
        val film: FilmDomainModel = filmLiveData.value!!
        film.isWatchLater = !film.isWatchLater
        filmLiveData.value = film
        Executors.newSingleThreadExecutor().execute {
            changeFilmLocalStateUseCase.execute(film)
        }
    }

    fun changeFavoritesField() {
        val film: FilmDomainModel = filmLiveData.value!!
        film.isFavorite = !film.isFavorite
        filmLiveData.value = film
        Executors.newSingleThreadExecutor().execute {
            changeFilmLocalStateUseCase.execute(film)
        }
    }

}