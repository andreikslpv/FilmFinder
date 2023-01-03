package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class SelectionsFragmentViewModel : ViewModel() {
    //Инициализируем usecases
    private var getPagedFilmsByCategoryUseCase = App.instance.getPagedFilmsByCategoryUseCase
    val filmsFlow: Flow<PagingData<FilmDomainModel>>

    private val language = MutableLiveData("")

    init {
        filmsFlow = language.asFlow().flatMapLatest { getPagedFilmsByCategoryUseCase.execute(it) }
            .cachedIn(viewModelScope)
    }

    fun setLanguage(newLanguage: String) {
        if (this.language.value == newLanguage) return
        else this.language.value = newLanguage
    }

    fun refresh() {
        this.language.postValue(this.language.value)
    }

}