package com.andreikslpv.filmfinder.data.datasource.api.kp2


import com.google.gson.annotations.SerializedName

data class Logo(
    @SerializedName("_id")
    val id: String,
    @SerializedName("url")
    val url: Any
)