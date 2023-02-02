package com.andreikslpv.filmfinder.data.datasource.local

import androidx.lifecycle.LiveData
import com.andreikslpv.filmfinder.data.datasource.local.dao.FilmDao
import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel
import javax.inject.Inject

class RoomLocalDataSource @Inject constructor(private val filmDao: FilmDao) : FilmsLocalDataSource {

    override fun getFavoritesFilms(): LiveData<List<FilmLocalModel>> {
        return filmDao.getFavoritesFilms()
    }

    override fun getWatchLaterFilms(): LiveData<List<FilmLocalModel>> {
        return filmDao.getWatchLaterFilms()
    }

    override fun getFilmLocalState(filmId: String): LiveData<FilmLocalModel> {
        return filmDao.getFilmById(filmId)
    }

    override fun saveFilm(film: FilmLocalModel) {
        filmDao.insertFilm(film)
    }
}