package com.andreikslpv.filmfinder.domain

import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel

interface FilmsRepository {

    fun getAllFilms(): List<FilmsLocalModel>

    fun changeFilmLocalState(film: FilmsLocalModel)

    fun getFilmLocalState(film: FilmsLocalModel): FilmsLocalModel

    fun getFavoriteFilms(): List<FilmsLocalModel>

    fun getWatchLaterFilms(): List<FilmsLocalModel>

}