package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.SettingsRepository
import com.andreikslpv.filmfinder.domain.types.SettingsType

class GetAllSettingValueUseCase(
    private val settingsRepository: SettingsRepository,
    private val filmsRepository: FilmsRepository
) {
    fun execute() {
        filmsRepository.setApiDataSource(settingsRepository.getSettingValue(SettingsType.API_TYPE))
        filmsRepository.setCacheMode(settingsRepository.getSettingValue(SettingsType.CACHE_MODE))
    }
}