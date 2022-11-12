package com.andreikslpv.filmfinder.data.datasource.api

import com.andreikslpv.filmfinder.data.datasource.api.models.FilmsApiModel

interface FilmsApiDataSource {

    fun getAllFilmsByPage(): List<FilmsApiModel>
}