package com.andreikslpv.filmfinder.data.datasource.api.kp


import com.google.gson.annotations.SerializedName

data class KpDtoExternalId(
    @SerializedName("imdb")
    val imdb: String,
    @SerializedName("tmdb")
    val tmdb: Int
)