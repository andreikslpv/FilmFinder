package com.andreikslpv.filmfinder.data.datasource.api.kp2


import com.google.gson.annotations.SerializedName

data class KpNew(
    @SerializedName("docs")
    val docs: List<Doc>,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)