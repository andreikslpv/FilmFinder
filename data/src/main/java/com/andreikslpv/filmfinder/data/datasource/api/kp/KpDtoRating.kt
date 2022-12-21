package com.andreikslpv.filmfinder.data.datasource.api.kp


import com.google.gson.annotations.SerializedName

data class KpDtoRating(
    @SerializedName("imdb")
    val imdb: Double,
    @SerializedName("kp")
    val kp: Double,
    @SerializedName("tmdb")
    val tmdb: Double
)