package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.ChangeFilmLocalStateUseCase
import javax.inject.Inject

class DetailsFragmentViewModel : ViewModel() {
    val filmLiveData: MutableLiveData<FilmDomainModel> = MutableLiveData()

    //Инициализируем usecases
    @Inject
    lateinit var changeFilmLocalStateUseCase: ChangeFilmLocalStateUseCase

    init {
        App.instance.dagger.inject(this)
    }

    fun setFilm(film: FilmDomainModel) {
        filmLiveData.value = film
    }

    fun changeWatchLaterField() {
        val film: FilmDomainModel = filmLiveData.value!!
        film.isWatchLater = !film.isWatchLater
        filmLiveData.value = film
        changeFilmLocalStateUseCase.execute(filmLiveData.value!!)
    }

    fun changeFavoritesField() {
        val film: FilmDomainModel = filmLiveData.value!!
        film.isFavorite = !film.isFavorite
        filmLiveData.value = film
        changeFilmLocalStateUseCase.execute(filmLiveData.value!!)
    }
}