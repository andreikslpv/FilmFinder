package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.domain.usecase.GetAvailableCategoriesUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetCurrentApiDataSourceUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetPagedFilmsByCategoryUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SelectionsFragmentViewModel : ViewModel() {
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

    private var previousApi = ValuesType.NONE

    //arrayOf(ValuesType.NONE, ValuesType.NONE)
    val currentApiFlow: StateFlow<ValuesType> by lazy {
        getCurrentApiDataSourceUseCase.execute().asStateFlow()
    }

    private val _currentCategoryList = MutableStateFlow(emptyList<CategoryType>())
    val currentCategoryList: StateFlow<List<CategoryType>> = _currentCategoryList.asStateFlow()


    init {
        App.instance.dagger.inject(this)

        setCategoryList()
        //categoryList = getAvailableCategoriesUseCase.execute()
        if (_currentCategoryList.value.isNotEmpty())
            _category.tryEmit(_currentCategoryList.value[0])

        filmsFlow = category
            .flatMapLatest { getPagedFilmsByCategoryUseCase.execute(it) }
            .cachedIn(viewModelScope)
    }

    fun setCategoryList() {
        viewModelScope.launch {
            getAvailableCategoriesUseCase.execute().collect {
//                println("!!! ${it}")
//                categoryList = it
//                println("!!! ${categoryList}")
//                if (it.isNotEmpty())
//                    _category.tryEmit(it[0])
                //viewModelScope.launch {
//                val job = scope.async {
//                    viewModel.loadWallpaper(viewModel.filmLocalStateLiveData.value!!.posterDetails)
//                }
                _currentCategoryList.tryEmit(it)
                //}
            }
        }
    }

    fun setCategory(newCategory: CategoryType) {
        if (newCategory == category.value) return
        else _category.tryEmit(newCategory)
    }

    fun isOldApi(api: ValuesType): Boolean {
        return if (api == previousApi) true
        else {
            previousApi = api
            false
        }
    }
}