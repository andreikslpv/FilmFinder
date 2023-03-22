package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.domain.usecase.apicache.GetAvailableCategoriesUseCase
import com.andreikslpv.filmfinder.domain.usecase.apicache.GetCurrentApiDataSourceUseCase
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectionsFragmentViewModel : ViewModel() {
    val currentApi: Observable<ValuesType>

    @Inject
    lateinit var getCurrentApiDataSourceUseCase: GetCurrentApiDataSourceUseCase

    @Inject
    lateinit var getAvailableCategoriesUseCase: GetAvailableCategoriesUseCase

    var categoryList: List<CategoryType> = emptyList()

    var isNewError = true

    init {
        App.instance.dagger.inject(this)
        currentApi = getCurrentApiDataSourceUseCase.execute()

        viewModelScope.launch {
            getAvailableCategoriesUseCase.execute().asStateFlow().collect {
                categoryList = it
            }
        }
    }

}