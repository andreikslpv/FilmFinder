package com.andreikslpv.filmfinder.data.datasource.local.models

data class FilmLocalModel (
    val id: Int,
    val title: String,
    val poster: Int,
    val description: String,
    var isFavorite: Boolean = false,
    var isWatchLater: Boolean = false
)