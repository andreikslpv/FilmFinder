package com.andreikslpv.filmfinder.data.datasource.api.kp2


import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("await")
    val await: Double,
    @SerializedName("filmCritics")
    val filmCritics: Int,
    @SerializedName("_id")
    val id: String,
    @SerializedName("imdb")
    val imdb: Int,
    @SerializedName("kp")
    val kp: Int,
    @SerializedName("russianFilmCritics")
    val russianFilmCritics: Int
)