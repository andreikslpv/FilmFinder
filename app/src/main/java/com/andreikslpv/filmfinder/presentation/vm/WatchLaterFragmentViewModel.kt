package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.GetSearchResultUseCase
import com.andreikslpv.filmfinder.domain.usecase.local.GetWatchLaterFilmsUseCase
import javax.inject.Inject

class WatchLaterFragmentViewModel : ViewModel() {
    val filmsListLiveData: LiveData<List<FilmDomainModel>>

    @Inject
    lateinit var getWatchLaterFilmsUseCase: GetWatchLaterFilmsUseCase
//
//    @Inject
//    lateinit var getSearchResultUseCase: GetSearchResultUseCase

    init {
        App.instance.dagger.inject(this)
        filmsListLiveData = getWatchLaterFilmsUseCase.execute()
    }

//    fun getWatchLaterFilms() {
//        filmsListLiveData.value = getWatchLaterFilmsUseCase.execute()
//    }
//
//    fun getSearchResult(searchText: String) {
//        filmsListLiveData.value =
//            getSearchResultUseCase.execute(searchText, getWatchLaterFilmsUseCase.execute())
//    }
}