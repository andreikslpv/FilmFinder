package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.domain.usecase.apicache.GetAvailableCategoriesUseCase
import com.andreikslpv.filmfinder.domain.usecase.apicache.GetCurrentApiDataSourceUseCase
import com.andreikslpv.filmfinder.domain.usecase.apicache.GetPagedFilmsByCategoryUseCase
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SelectionsFragmentViewModel : ViewModel() {
    val currentApi: Observable<ValuesType>
    @Inject
    lateinit var getPagedFilmsByCategoryUseCase: GetPagedFilmsByCategoryUseCase

    @Inject
    lateinit var getCurrentApiDataSourceUseCase: GetCurrentApiDataSourceUseCase

    @Inject
    lateinit var getAvailableCategoriesUseCase: GetAvailableCategoriesUseCase

    var categoryList: List<CategoryType> = emptyList()

    private val _category = MutableStateFlow(CategoryType.NONE)
    private val category: StateFlow<CategoryType> = _category.asStateFlow()

    val filmsFlow: Flow<PagingData<FilmDomainModel>>

    var isNewError = true

    init {
        App.instance.dagger.inject(this)
        currentApi = getCurrentApiDataSourceUseCase.execute()

        viewModelScope.launch {
            getAvailableCategoriesUseCase.execute().asStateFlow().collect {
                categoryList = it
            }
        }
        if (categoryList.isNotEmpty())
            _category.tryEmit(categoryList[0])

        filmsFlow = category
            .flatMapLatest { getPagedFilmsByCategoryUseCase.execute(it) }
            .cachedIn(viewModelScope)
    }

    fun setCategory(newCategory: CategoryType) {
        if (newCategory == category.value) return
        else {
            isNewError = true
            _category.tryEmit(newCategory)
        }
    }

}