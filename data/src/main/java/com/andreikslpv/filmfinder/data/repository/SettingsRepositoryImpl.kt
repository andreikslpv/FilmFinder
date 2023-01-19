package com.andreikslpv.filmfinder.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.andreikslpv.filmfinder.domain.SettingsRepository
import com.andreikslpv.filmfinder.domain.types.SettingsType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(context: Context) : SettingsRepository {
    //Создаем экземпляр SharedPreferences
    private val preference: SharedPreferences =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    init {
        //Логика для первого запуска приложения, чтобы положить дефолтные настройки,
        if (preference.getBoolean(SettingsType.FIRST_LAUNCH.key, true)) {
            preference.edit()
                .putString(SettingsType.API_TYPE.key, SettingsType.API_TYPE.defaultValue.value)
                .apply()
            preference.edit()
                .putString(SettingsType.CACHE_MODE.key, SettingsType.CACHE_MODE.defaultValue.value)
                .apply()
            preference.edit().putBoolean(SettingsType.FIRST_LAUNCH.key, false).apply()
        }
    }

    override fun getSettingValue(setting: SettingsType): ValuesType {
        return getValuesTypeByValue(
            preference.getString(setting.key, setting.defaultValue.value)
                ?: setting.defaultValue.value
        )
    }

    private fun getValuesTypeByValue(value: String): ValuesType {
        return when (value) {
            ValuesType.TMDB.value -> ValuesType.TMDB
            ValuesType.IMDB.value -> ValuesType.IMDB
            ValuesType.AUTO.value -> ValuesType.AUTO
            ValuesType.ALWAYS.value -> ValuesType.ALWAYS
            ValuesType.NEVER.value -> ValuesType.NEVER
            else -> ValuesType.NONE
        }
    }

    override fun setSettingValue(setting: SettingsType, value: ValuesType) {
        preference.edit().putString(setting.key, value.value).apply()
    }
}