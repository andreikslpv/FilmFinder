package com.andreikslpv.filmfinder.data.datasource.api.models

data class FilmApiModel(
    val id: Int,
    val title: String,
    val posterPreview: String,
    val posterDetails: String,
    val description: String,
    var rating: Double = 0.0
)