package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.local.GetFavoritesFilmsUseCase
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FavoritesFragmentViewModel : ViewModel() {
    val filmsList: Observable<List<FilmDomainModel>>

    @Inject
    lateinit var getFavoritesFilmsUseCase: GetFavoritesFilmsUseCase

    init {
        App.instance.dagger.inject(this)
        filmsList = getFavoritesFilmsUseCase.execute()
    }

}