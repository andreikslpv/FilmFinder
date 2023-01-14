package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.types.SettingsType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.domain.usecase.GetSettingValueUseCase
import com.andreikslpv.filmfinder.domain.usecase.SetApiDataSourceUseCase
import com.andreikslpv.filmfinder.domain.usecase.SetSettingValueUseCase
import javax.inject.Inject

class SettingsFragmentViewModel : ViewModel() {
    @Inject
    lateinit var getSettingValueUseCase: GetSettingValueUseCase
    @Inject
    lateinit var setSettingValueUseCase: SetSettingValueUseCase
    @Inject
    lateinit var setApiDataSourceUseCase: SetApiDataSourceUseCase

    val apiLiveData: MutableLiveData<ValuesType> = MutableLiveData()

    init {
        App.instance.dagger.inject(this)
        getApiType()
    }

    private fun getApiType() {
        apiLiveData.value = getSettingValueUseCase.execute(SettingsType.API_TYPE)
    }

    fun setApiType(api: ValuesType) {
        if (api != apiLiveData.value) {
            setApiDataSourceUseCase.execute(api)
            setSettingValueUseCase.execute(SettingsType.API_TYPE, api)
            getApiType()
        }
    }
}