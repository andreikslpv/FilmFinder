package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.SettingsRepository
import com.andreikslpv.filmfinder.domain.types.SettingsType
import com.andreikslpv.filmfinder.domain.types.ValuesType

class GetSettingValueUseCase(private val settingsRepository: SettingsRepository) {
    fun execute(setting: SettingsType) : ValuesType {
        return settingsRepository.getSettingValue(setting)
    }
}