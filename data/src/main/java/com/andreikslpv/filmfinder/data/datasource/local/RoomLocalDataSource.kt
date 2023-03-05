package com.andreikslpv.filmfinder.data.datasource.local

import com.andreikslpv.filmfinder.data.datasource.local.dao.FilmDao
import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RoomLocalDataSource @Inject constructor(private val filmDao: FilmDao) : FilmsLocalDataSource {

    override fun getFavoritesFilms(): Observable<List<FilmLocalModel>> {
        return filmDao.getFavoritesFilms()
    }

    override fun getWatchLaterFilms(): Observable<List<FilmLocalModel>> {
        return filmDao.getWatchLaterFilms()
    }

    override fun getFilmLocalState(filmId: String): Observable<FilmLocalModel> {
        return filmDao.getFilmById(filmId)
    }

    override fun saveFilm(film: FilmLocalModel, replace: Boolean) {
        if (replace)
            filmDao.updateFilm(film)
        else
            filmDao.insertFilm(film)
    }
}