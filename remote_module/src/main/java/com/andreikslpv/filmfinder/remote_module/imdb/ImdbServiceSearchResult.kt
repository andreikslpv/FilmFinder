package com.andreikslpv.filmfinder.remote_module.imdb

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ImdbServiceSearchResult {
    @GET("{language}/{api}/{search}/{key}/{query}")
    fun getFilms(
        @Path("language") language: String,
        @Path("api") api: String = ImdbConstants.API,
        @Path("search") search: String = ImdbConstants.SEARCH,
        @Path("key") key: String = ImdbKey.KEY,
        @Path("query") query: String
    ): Single<ImdbDtoSearchResults>
}