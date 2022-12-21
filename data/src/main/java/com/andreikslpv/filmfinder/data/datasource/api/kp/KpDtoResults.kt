package com.andreikslpv.filmfinder.data.datasource.api.kp


import com.google.gson.annotations.SerializedName

data class KpDtoResults(
    @SerializedName("docs")
    val docs: List<KpDtoDoc>,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)