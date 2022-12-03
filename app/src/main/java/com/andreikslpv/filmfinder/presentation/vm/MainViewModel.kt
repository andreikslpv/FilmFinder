package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.ChangeFilmLocalStateUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetAllFilmsByPageUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetFilmLocalStateUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetSearchResultUseCase

class MainViewModel(
    private val getAllFilmsByPageUseCase: GetAllFilmsByPageUseCase,
    private val getFilmLocalStateUseCase: GetFilmLocalStateUseCase,
    private val changeFilmLocalStateUseCase: ChangeFilmLocalStateUseCase,
    private val getSearchResultUseCase: GetSearchResultUseCase
) :
    ViewModel() {

    var allFilmsLive = MutableLiveData<List<FilmDomainModel>>()
    var selectedFilm = MutableLiveData<FilmDomainModel>()

    fun getAllFilmsByPage() {
        allFilmsLive.value = getAllFilmsByPageUseCase.execute()
    }

    fun loadSelectedFilm(film: FilmDomainModel) {
        selectedFilm.value = getFilmLocalStateUseCase.execute(film)
    }

    fun changeWatchLaterField() {
//        val film: FilmDomainModel = selectedFilm.value!!
//        film.isWatchLater = !film.isWatchLater
//        selectedFilm.value = film
        if (selectedFilm.value != null) {
            selectedFilm.value!!.isWatchLater = !selectedFilm.value!!.isWatchLater
            //changeFilmLocalStateUseCase.execute(selectedFilm.value!!)

        }
    }

    fun changeFavoritesField() {
        if (selectedFilm.value != null) {
            selectedFilm.value!!.isFavorite = !selectedFilm.value!!.isFavorite
            changeFilmLocalStateUseCase.execute(selectedFilm.value!!)
        }
    }
}