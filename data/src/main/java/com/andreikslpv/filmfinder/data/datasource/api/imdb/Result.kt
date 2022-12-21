package com.andreikslpv.filmfinder.data.datasource.api.imdb

data class Result(
    val description: String,
    val id: String,
    val image: String,
    val resultType: String,
    val title: String
)