package com.andreikslpv.filmfinder.data.datasource.api

import com.andreikslpv.filmfinder.data.datasource.api.models.FilmApiModel

interface FilmsApiDataSource {

    fun getAllFilmsByPage(): List<FilmApiModel>
}