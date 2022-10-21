package com.andreikslpv.filmfinder.domain

import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel

interface FilmsRepository {
    fun getAllFilms(): List<FilmsLocalModel>

    fun changeFilmsState(film: FilmsLocalModel)

    fun getFavoriteFilms(): List<FilmsLocalModel>

    fun getWatchLaterFilms(): List<FilmsLocalModel>

    fun getAllFilmsWithFavoritesChecked(): List<FilmsLocalModel>
}