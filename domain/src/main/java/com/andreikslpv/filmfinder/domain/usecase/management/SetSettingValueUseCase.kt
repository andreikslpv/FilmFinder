package com.andreikslpv.filmfinder.domain.usecase.management

import com.andreikslpv.filmfinder.domain.SettingsRepository
import com.andreikslpv.filmfinder.domain.types.SettingsType
import com.andreikslpv.filmfinder.domain.types.ValuesType

class SetSettingValueUseCase(private val settingsRepository: SettingsRepository) {
    fun execute(setting: SettingsType, value: ValuesType) {
        settingsRepository.setSettingValue(setting, value)
    }
}