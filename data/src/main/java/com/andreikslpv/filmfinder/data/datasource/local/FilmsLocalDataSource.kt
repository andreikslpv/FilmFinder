package com.andreikslpv.filmfinder.data.datasource.local

import androidx.lifecycle.LiveData
import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel

interface FilmsLocalDataSource {

    fun getFavoritesFilms(): LiveData<List<FilmLocalModel>>

    fun getWatchLaterFilms(): LiveData<List<FilmLocalModel>>

    fun getFilmLocalState(filmId: String): LiveData<FilmLocalModel>

    fun saveFilm(film: FilmLocalModel)
}