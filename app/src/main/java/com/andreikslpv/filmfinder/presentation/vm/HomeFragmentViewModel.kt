package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class HomeFragmentViewModel : ViewModel() {
    val filmsListLiveData: MutableLiveData<List<FilmDomainModel>> = MutableLiveData()

    //Инициализируем usecases
    private var getAllFilmsByPageUseCase = App.instance.getAllFilmsByPageUseCase
    private var getSearchResultUseCase = App.instance.getSearchResultUseCase

    fun getAllFilmsByPage() {
        filmsListLiveData.value = getAllFilmsByPageUseCase.execute()
    }

    fun getSearchResult(searchText: String) {
        filmsListLiveData.value =
            getSearchResultUseCase.execute(searchText, getAllFilmsByPageUseCase.execute())
    }
}