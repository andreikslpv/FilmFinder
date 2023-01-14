package com.andreikslpv.filmfinder.data.datasource.api.imdb


import com.google.gson.annotations.SerializedName

data class ImdbDtoSearchResults(
    @SerializedName("errorMessage")
    val errorMessage: String?,
    @SerializedName("expression")
    val expression: String?,
    @SerializedName("results")
    val results: List<ImdbDtoSearchItem?>?,
    @SerializedName("searchType")
    val searchType: String?
)