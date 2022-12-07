package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class FavoritesFragmentViewModel : ViewModel() {
    val filmsListLiveData: MutableLiveData<List<FilmDomainModel>> = MutableLiveData()

    //Инициализируем usecases
    private var getFavoritesFilmsUseCase = App.instance.getFavoritesFilmsUseCase
    private var getSearchResultUseCase = App.instance.getSearchResultUseCase

    fun getFavoritesFilms() {
        filmsListLiveData.value = getFavoritesFilmsUseCase.execute()
    }

    fun getSearchResult(searchText: String) {
        filmsListLiveData.value =
            getSearchResultUseCase.execute(searchText, getFavoritesFilmsUseCase.execute())
    }
}