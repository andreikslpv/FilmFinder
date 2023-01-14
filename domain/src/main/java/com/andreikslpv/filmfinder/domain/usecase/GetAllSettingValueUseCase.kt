package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.SettingsRepository
import com.andreikslpv.filmfinder.domain.types.SettingsType
import com.andreikslpv.filmfinder.domain.types.ValuesType

class GetAllSettingValueUseCase(private val settingsRepository: SettingsRepository) {
    fun execute(): Map<SettingsType, ValuesType> {
        val map = emptyMap<SettingsType, ValuesType>()
        map.plus(Pair(SettingsType.API_TYPE, settingsRepository.getSettingValue(SettingsType.API_TYPE)))
        return map
    }
}