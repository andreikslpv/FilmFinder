package com.andreikslpv.filmfinder.data.datasource.local.models

data class FilmLocalModel (
    val id: Int,
    val title: String,
    val posterPreview: String,
    val posterDetails: String,
    val description: String,
    var rating: Double = 0.0,
    var isFavorite: Boolean = false,
    var isWatchLater: Boolean = false
)