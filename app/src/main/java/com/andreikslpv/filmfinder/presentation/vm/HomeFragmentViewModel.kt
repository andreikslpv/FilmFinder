package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.domain.usecase.apicache.GetCurrentApiDataSourceUseCase
import com.andreikslpv.filmfinder.domain.usecase.apicache.GetPagedSearchResultUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class HomeFragmentViewModel : ViewModel() {
    @Inject
    lateinit var getPagedSearchResultUseCase: GetPagedSearchResultUseCase

    @Inject
    lateinit var getCurrentApiDataSourceUseCase: GetCurrentApiDataSourceUseCase

    val filmsFlow: Flow<PagingData<FilmDomainModel>>

    private val _currentQuery = MutableStateFlow("")
    private val currentQuery: StateFlow<String> = _currentQuery.asStateFlow()

    private var previousApi = ValuesType.NONE
    val currentApiFlow: StateFlow<ValuesType> by lazy {
        getCurrentApiDataSourceUseCase.execute().asStateFlow()
    }

    var isNewError = true

    init {
        App.instance.dagger.inject(this)

        filmsFlow = currentQuery
            // исключаем слишком частые запросы, если пользователь быстро вводит поисковой запрос
            .debounce(500)
            .flatMapLatest { getPagedSearchResultUseCase.execute(it) }
            // кешируем прлучившийся flow, чтобы на него можно было подписаться несколько раз
            .cachedIn(viewModelScope)
    }

    fun setQuery(newQuery: String) {
        if (currentQuery.value == newQuery) return
        else {
            isNewError = true
            _currentQuery.tryEmit(newQuery)
        }
    }

    fun isNewApi(api: ValuesType): Boolean {
        return if (api != previousApi) {
            previousApi = api
            true
        } else {
            false
        }
    }
}