package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.data.R
import com.andreikslpv.filmfinder.domain.ApiCallback
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class HomeFragmentViewModel : ViewModel() {
    val filmsListLiveData: MutableLiveData<List<FilmDomainModel>> = MutableLiveData()

    //Инициализируем usecases
    private var getSearchResultFromApiUseCase = App.instance.getSearchResultFromApiUseCase
    private var getFilmsFromApiUseCase = App.instance.getFilmsFromApiUseCase

    init {
        getFilms()
    }

    fun getFilms() {
        getFilmsFromApiUseCase.execute(
            1,
            App.instance.getString(R.string.tmdb_language),
            object : ApiCallback {
                override fun onSuccess(films: List<FilmDomainModel>) {
                    filmsListLiveData.postValue(films)
                }

                override fun onFailure(message: String) {
                }
            })
    }

    fun getSearchResult(searchText: String) {
        getSearchResultFromApiUseCase.execute(
            searchText,
            1,
            App.instance.getString(R.string.tmdb_language),
            object : ApiCallback {
                override fun onSuccess(films: List<FilmDomainModel>) {
                    filmsListLiveData.postValue(films)
                }

                override fun onFailure(message: String) {
                }
            })
    }
}