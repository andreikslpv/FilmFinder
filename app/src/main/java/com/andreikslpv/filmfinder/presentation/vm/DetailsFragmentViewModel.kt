package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class DetailsFragmentViewModel : ViewModel() {
    val filmLiveData: MutableLiveData<FilmDomainModel> = MutableLiveData()

    //Инициализируем usecases
    private var changeFilmLocalStateUseCase = App.instance.changeFilmLocalStateUseCase

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