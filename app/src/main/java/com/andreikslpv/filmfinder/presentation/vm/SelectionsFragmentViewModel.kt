package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.domain.usecase.GetCurrentApiDataSourceUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetPagedFilmsByCategoryUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SelectionsFragmentViewModel : ViewModel() {
    @Inject
    lateinit var getPagedFilmsByCategoryUseCase: GetPagedFilmsByCategoryUseCase

    @Inject
    lateinit var getCurrentApiDataSourceUseCase: GetCurrentApiDataSourceUseCase

    lateinit var filmsFlow: Flow<PagingData<FilmDomainModel>>
    // для отслеживания момента инициализации filmsFlow
    private val _filmsFlowInitStatus = MutableLiveData(false)
    val filmsFlowInitStatus: LiveData<Boolean> = _filmsFlowInitStatus

    private val currentCategory = MutableLiveData(CategoryType.NONE)
    val apiLiveData: MutableLiveData<ValuesType> = MutableLiveData()

    init {
        App.instance.dagger.inject(this)
    }

    fun setCategory(newCategory: CategoryType) {
        if (this.currentCategory.value == newCategory) return
        else {
            this.currentCategory.value = newCategory
            if (_filmsFlowInitStatus.value == false)
                initFilmFlow()
        }
    }

    fun refresh() {
        this.currentCategory.postValue(this.currentCategory.value)
    }

    fun setApiType() {
        val newApi = getCurrentApiDataSourceUseCase.execute()
        if (newApi == apiLiveData.value) return
        else apiLiveData.value = newApi
    }

    private fun initFilmFlow() {
        filmsFlow = currentCategory
            .asFlow()
            .flatMapLatest { getPagedFilmsByCategoryUseCase.execute(it) }
            .cachedIn(viewModelScope)
        _filmsFlowInitStatus.value = true
    }
}