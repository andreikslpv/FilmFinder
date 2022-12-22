package com.andreikslpv.filmfinder.data.datasource.api.kp2


import com.google.gson.annotations.SerializedName

data class Doc(
    @SerializedName("alternativeName")
    val alternativeName: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("enName")
    val enName: Any,
    @SerializedName("externalId")
    val externalId: ExternalId,
    @SerializedName("id")
    val id: Int,
    @SerializedName("logo")
    val logo: Logo,
    @SerializedName("movieLength")
    val movieLength: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("names")
    val names: List<Name>,
    @SerializedName("poster")
    val poster: Any,
    @SerializedName("rating")
    val rating: Rating,
    @SerializedName("releaseYears")
    val releaseYears: List<Any>,
    @SerializedName("shortDescription")
    val shortDescription: Any,
    @SerializedName("type")
    val type: String,
    @SerializedName("votes")
    val votes: Votes,
    @SerializedName("watchability")
    val watchability: Watchability,
    @SerializedName("year")
    val year: Int
)