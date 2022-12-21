package com.andreikslpv.filmfinder.data.datasource.api.imdb

data class ImdbDtoResults(
    val errorMessage: String,
    val expression: String,
    val results: List<Result>,
    val searchType: String
)