package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.domain.usecase.apicache.GetCurrentApiDataSourceUseCase
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {
    val currentApi: Observable<ValuesType>
    @Inject
    lateinit var getCurrentApiDataSourceUseCase: GetCurrentApiDataSourceUseCase

    init {
        App.instance.dagger.inject(this)
        currentApi = getCurrentApiDataSourceUseCase.execute()
    }

}