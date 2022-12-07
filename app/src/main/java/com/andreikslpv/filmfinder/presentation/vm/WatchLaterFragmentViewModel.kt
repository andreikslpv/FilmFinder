package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class WatchLaterFragmentViewModel : ViewModel() {
    val filmsListLiveData: MutableLiveData<List<FilmDomainModel>> = MutableLiveData()

    //Инициализируем usecases
    private var getWatchLaterFilmsUseCase = App.instance.getWatchLaterFilmsUseCase
    private var getSearchResultUseCase = App.instance.getSearchResultUseCase

    fun getWatchLaterFilms() {
        filmsListLiveData.value = getWatchLaterFilmsUseCase.execute()
    }

    fun getSearchResult(searchText: String) {
        filmsListLiveData.value =
            getSearchResultUseCase.execute(searchText, getWatchLaterFilmsUseCase.execute())
    }
}