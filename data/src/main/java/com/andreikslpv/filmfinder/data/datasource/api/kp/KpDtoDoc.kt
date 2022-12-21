package com.andreikslpv.filmfinder.data.datasource.api.kp


import com.google.gson.annotations.SerializedName

data class KpDtoDoc(
    @SerializedName("description")
    val description: String,
    @SerializedName("externalId")
    val externalId: KpDtoExternalId,
    @SerializedName("id")
    val id: Int,
    @SerializedName("movieLength")
    val movieLength: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("poster")
    val poster: KpDtoPoster,
    @SerializedName("rating")
    val rating: KpDtoRating,
    @SerializedName("type")
    val type: String,
    @SerializedName("votes")
    val votes: KpDtoVotes,
    @SerializedName("year")
    val year: Int
)