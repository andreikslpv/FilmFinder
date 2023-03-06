package com.andreikslpv.filmfinder.data.datasource.local

import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel
import io.reactivex.rxjava3.core.Observable

interface FilmsLocalDataSource {

    fun getFavoritesFilms(): Observable<List<FilmLocalModel>>

    fun getWatchLaterFilms(): Observable<List<FilmLocalModel>>

    fun getFilmLocalState(filmId: String): Observable<FilmLocalModel>

    fun saveFilm(film: FilmLocalModel, replace: Boolean)
}