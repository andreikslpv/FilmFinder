package com.andreikslpv.filmfinder.data.datasource.api.imdb

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ImdbServiceFilmsByCategory {
    @GET("{language}/{api}/{category}/{key}")
    fun getFilms(
        @Path("language") language: String,
        @Path("api") api: String = ImdbConstants.API,
        @Path("category") category: String,
        @Path("key") key: String = ImdbKey.KEY,
    ): Single<ImdbDtoCategoryResults>
}