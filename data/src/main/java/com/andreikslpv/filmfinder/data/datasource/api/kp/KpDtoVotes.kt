package com.andreikslpv.filmfinder.data.datasource.api.kp


import com.google.gson.annotations.SerializedName

data class KpDtoVotes(
    @SerializedName("imdb")
    val imdb: Int?,
    @SerializedName("kp")
    val kp: Int?,
    @SerializedName("tmdb")
    val tmdb: Int?
)