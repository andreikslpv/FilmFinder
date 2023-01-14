package com.andreikslpv.filmfinder.data.datasource.api.imdb


import com.google.gson.annotations.SerializedName

data class ImdbDtoCategoryResults(
    @SerializedName("errorMessage")
    val errorMessage: String?,
    @SerializedName("items")
    val items: List<ImdbDtoCategoryItem?>?
)