package com.andreikslpv.filmfinder.domain

import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel

interface FilmsRepository {
    fun getAllFilms(): List<FilmsLocalModel>

    fun getAd(): List<FilmsLocalModel>

    fun saveFilm(film: FilmsLocalModel): Boolean

    fun removeFilm(id: Int): Boolean

    fun getFavoriteFilms(): List<FilmsLocalModel>

    fun getWatchLaterFilms(): List<FilmsLocalModel>
}