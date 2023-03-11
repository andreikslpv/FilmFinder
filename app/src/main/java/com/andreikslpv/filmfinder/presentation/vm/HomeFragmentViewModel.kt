package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.domain.usecase.apicache.GetCurrentApiDataSourceUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {

    @Inject
    lateinit var getCurrentApiDataSourceUseCase: GetCurrentApiDataSourceUseCase

    private var previousApi = ValuesType.NONE
    val currentApiFlow: StateFlow<ValuesType> by lazy {
        getCurrentApiDataSourceUseCase.execute().asStateFlow()
    }

    init {
        App.instance.dagger.inject(this)
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