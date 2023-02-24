package com.andreikslpv.filmfinder.data.datasource.local

import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel
import kotlinx.coroutines.flow.Flow

interface FilmsLocalDataSource {

    fun getFavoritesFilms(): Flow<List<FilmLocalModel>>

    fun getWatchLaterFilms(): Flow<List<FilmLocalModel>>

    fun getFilmLocalState(filmId: String): Flow<FilmLocalModel>

    fun saveFilm(film: FilmLocalModel)
}