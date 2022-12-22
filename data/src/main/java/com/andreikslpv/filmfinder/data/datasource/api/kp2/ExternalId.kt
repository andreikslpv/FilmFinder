package com.andreikslpv.filmfinder.data.datasource.api.kp2


import com.google.gson.annotations.SerializedName

data class ExternalId(
    @SerializedName("_id")
    val id: String,
    @SerializedName("imdb")
    val imdb: String,
    @SerializedName("kpHD")
    val kpHD: Any
)