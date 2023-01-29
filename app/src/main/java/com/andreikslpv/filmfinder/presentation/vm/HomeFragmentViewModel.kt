package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.domain.usecase.GetCurrentApiDataSourceUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetPagedSearchResultUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class HomeFragmentViewModel : ViewModel() {
    //Инициализируем usecases
    @Inject
    lateinit var getPagedSearchResultUseCase: GetPagedSearchResultUseCase

    @Inject
    lateinit var getCurrentApiDataSourceUseCase: GetCurrentApiDataSourceUseCase

    lateinit var filmsFlow: Flow<PagingData<FilmDomainModel>>

    // для отслеживания момента инициализации filmsFlow
    private val _filmsFlowInitStatus = MutableLiveData(false)
    val filmsFlowInitStatus: LiveData<Boolean> = _filmsFlowInitStatus

    private val currentQuery = MutableLiveData("")
    val apiLiveData: MutableLiveData<ValuesType> = MutableLiveData()

    init {
        App.instance.dagger.inject(this)
    }

    fun setQuery(newQuery: String) {
        if (this.currentQuery.value == newQuery) return
        else {
            this.currentQuery.value = newQuery
            if (_filmsFlowInitStatus.value == false)
                initFilmFlow()
        }
    }

    fun refresh() {
        this.currentQuery.postValue(this.currentQuery.value)
    }

    fun setApiType() {
        val newApi = getCurrentApiDataSourceUseCase.execute()
        if (newApi == apiLiveData.value) return
        else apiLiveData.value = newApi
    }

    private fun initFilmFlow() {
        filmsFlow = currentQuery
            .asFlow()
            // исключаем слишком частые запросы, если пользователь быстро вводит поисковой запрос
            .debounce(500)
            .flatMapLatest { getPagedSearchResultUseCase.execute(it) }
            // кешируем прлучившийся flow, чтобы на него можно было подписаться несколько раз
            .cachedIn(viewModelScope)
        _filmsFlowInitStatus.value = true
    }
}