package com.andreikslpv.filmfinder.remote_module.imdb


import com.google.gson.annotations.SerializedName

data class ImdbDtoCategoryResults(
    @SerializedName("errorMessage")
    val errorMessage: String?,
    @SerializedName("items")
    val items: List<ImdbDtoCategoryItem?>?
)