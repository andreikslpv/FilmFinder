package com.andreikslpv.filmfinder.domain

import com.andreikslpv.filmfinder.domain.types.SettingsType
import com.andreikslpv.filmfinder.domain.types.ValuesType

interface SettingsRepository {

    fun getSettingValue(setting: SettingsType): ValuesType

    fun setSettingValue(setting: SettingsType, value: ValuesType)
}