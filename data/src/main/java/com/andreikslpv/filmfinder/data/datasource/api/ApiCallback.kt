package com.andreikslpv.filmfinder.data.datasource.api

import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

interface ApiCallback {
    fun onSuccess(films: List<FilmDomainModel>)
    fun onFailure()
}