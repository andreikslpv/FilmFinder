package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.GetFavoritesFilmsUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetSearchResultUseCase
import javax.inject.Inject

class FavoritesFragmentViewModel : ViewModel() {
    val filmsListLiveData: MutableLiveData<List<FilmDomainModel>> = MutableLiveData()

    //Инициализируем usecases
    @Inject
    lateinit var getFavoritesFilmsUseCase: GetFavoritesFilmsUseCase
    @Inject
    lateinit var getSearchResultUseCase:GetSearchResultUseCase

    init {
        App.instance.dagger.inject(this)
    }

    fun getFavoritesFilms() {
        filmsListLiveData.value = getFavoritesFilmsUseCase.execute()
    }

    fun getSearchResult(searchText: String) {
        filmsListLiveData.value =
            getSearchResultUseCase.execute(searchText, getFavoritesFilmsUseCase.execute())
    }
}