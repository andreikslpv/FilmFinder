package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.local.GetFavoritesFilmsUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetSearchResultUseCase
import javax.inject.Inject

class FavoritesFragmentViewModel : ViewModel() {
    val filmsListLiveData: LiveData<List<FilmDomainModel>>

    @Inject
    lateinit var getFavoritesFilmsUseCase: GetFavoritesFilmsUseCase
//    @Inject
//    lateinit var getSearchResultUseCase:GetSearchResultUseCase

    init {
        App.instance.dagger.inject(this)
        filmsListLiveData = getFavoritesFilmsUseCase.execute()
    }

//    fun getFavoritesFilms() {
//        filmsListLiveData.value = getFavoritesFilmsUseCase.execute()
//    }

//    fun getSearchResult(searchText: String) {
//        filmsListLiveData.value =
//            getSearchResultUseCase.execute(searchText, getFavoritesFilmsUseCase.execute())
//    }
}