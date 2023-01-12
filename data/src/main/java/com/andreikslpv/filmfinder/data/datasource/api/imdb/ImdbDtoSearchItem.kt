package com.andreikslpv.filmfinder.data.datasource.api.imdb


import com.google.gson.annotations.SerializedName

data class ImdbDtoSearchItem(
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("resultType")
    val resultType: String?,
    @SerializedName("title")
    val title: String?
)