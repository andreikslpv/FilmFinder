package com.andreikslpv.filmfinder.data.datasource.api.imdb

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ImdbServiceSearchResult {
    @GET("{language}/{api}/{search}/{key}/{query}")
    suspend fun getFilms(
        @Path("language") language: String,
        @Path("api") api: String = ImdbConstants.API,
        @Path("search") search: String = ImdbConstants.SEARCH,
        @Path("key") key: String = ImdbKey.KEY,
        @Path("query") query: String
    ): Response<ImdbDtoSearchResults>
}