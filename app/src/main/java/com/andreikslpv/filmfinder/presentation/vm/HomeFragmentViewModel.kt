package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
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
    val filmsFlow: Flow<PagingData<FilmDomainModel>>
    private val currentQuery = MutableLiveData("")

    init {
        App.instance.dagger.inject(this)

        filmsFlow = currentQuery
            .asFlow()
            // исключаем слишком частые запросы, если пользователь быстро вводит поисковой запрос
            .debounce(500)
            .flatMapLatest { getPagedSearchResultUseCase.execute(it) }
            // кешируем прлучившийся flow, чтобы на него можно было подписаться несколько раз
            .cachedIn(viewModelScope)
    }

    fun setQuery(newQuery: String) {
        if (this.currentQuery.value == newQuery) return
        else this.currentQuery.value = newQuery
    }

    fun refresh() {
        this.currentQuery.postValue(this.currentQuery.value)
    }
}