package com.andreikslpv.filmfinder.data.datasource.api.kp2


import com.google.gson.annotations.SerializedName

data class Watchability(
    @SerializedName("_id")
    val id: String,
    @SerializedName("items")
    val items: Any
)