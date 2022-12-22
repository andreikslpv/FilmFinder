package com.andreikslpv.filmfinder.data.datasource.api.kp2


import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String
)