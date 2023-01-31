package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.local.GetFavoritesFilmsUseCase
import javax.inject.Inject

class FavoritesFragmentViewModel : ViewModel() {
    val filmsListLiveData: LiveData<List<FilmDomainModel>>

    @Inject
    lateinit var getFavoritesFilmsUseCase: GetFavoritesFilmsUseCase

    init {
        App.instance.dagger.inject(this)
        filmsListLiveData = getFavoritesFilmsUseCase.execute()
    }

}