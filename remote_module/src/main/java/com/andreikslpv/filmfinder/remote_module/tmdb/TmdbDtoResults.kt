package com.andreikslpv.filmfinder.remote_module.tmdb

import com.google.gson.annotations.SerializedName

data class TmdbDtoResults(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<TmdbDtoFilm>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
