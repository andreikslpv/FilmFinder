package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.SettingsRepository
import com.andreikslpv.filmfinder.domain.types.SettingsType
import com.andreikslpv.filmfinder.domain.types.ValuesType

class GetAllSettingValueUseCase(
    private val settingsRepository: SettingsRepository,
    private val filmsRepository: FilmsRepository
) {
    fun execute() {
        filmsRepository.setApiDataSource(settingsRepository.getSettingValue(SettingsType.API_TYPE))
        filmsRepository.setCacheMode(settingsRepository.getSettingValue(SettingsType.CACHE_MODE))

//        val map = mutableMapOf<SettingsType, ValuesType>()
//        map[SettingsType.API_TYPE] = settingsRepository.getSettingValue(SettingsType.API_TYPE)
//        map[SettingsType.CACHE_MODE] = settingsRepository.getSettingValue(SettingsType.CACHE_MODE)
    }
}