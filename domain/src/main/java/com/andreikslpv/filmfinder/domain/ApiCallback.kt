package com.andreikslpv.filmfinder.domain

import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

interface ApiCallback {
    fun onSuccess(films: List<FilmDomainModel>)
    fun onFailure(message: String)
}