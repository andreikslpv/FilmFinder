package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.GetPagedFilmsByCategoryUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SelectionsFragmentViewModel : ViewModel() {
    //Инициализируем usecases
    @Inject
    lateinit var getPagedFilmsByCategoryUseCase: GetPagedFilmsByCategoryUseCase
    val filmsFlow: Flow<PagingData<FilmDomainModel>>
    private val currentCategory = MutableLiveData(CategoryType.NONE)

    init {
        App.instance.dagger.inject(this)

        filmsFlow = currentCategory
            .asFlow()
            .flatMapLatest { getPagedFilmsByCategoryUseCase.execute(it) }
            .cachedIn(viewModelScope)
    }

    fun setCategory(newCategory: CategoryType) {
        if (this.currentCategory.value == newCategory) return
        else this.currentCategory.value = newCategory
    }

    fun refresh() {
        this.currentCategory.postValue(this.currentCategory.value)
    }

}