package com.andreikslpv.filmfinder.domain

import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel

interface FilmsRepository {
    fun getAllFilms(): List<FilmsLocalModel>
    fun getAd(): List<FilmsLocalModel>
}