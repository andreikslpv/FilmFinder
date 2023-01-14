package com.andreikslpv.filmfinder.domain.types

enum class SettingsType(val key: String, val defaultValue: ValuesType) {
    API_TYPE("api_type", ValuesType.IMDB),
    FIRST_LAUNCH("first_launch", ValuesType.NONE),
}