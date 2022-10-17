package com.andreikslpv.filmfinder.domain

import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel

interface FilmsRepository {
    fun getAllFilms(): List<FilmsLocalModel>

    fun getAd(): List<FilmsLocalModel>

    fun changeFilmsState(film: FilmsLocalModel)

    fun saveFilm(film: FilmsLocalModel): Boolean

    fun removeFilm(film: FilmsLocalModel): Boolean

    fun getFavoriteFilms(): List<FilmsLocalModel>

    fun getWatchLaterFilms(): List<FilmsLocalModel>

    fun getAllFilmsWithFavoritesChecked(): List<FilmsLocalModel>
}