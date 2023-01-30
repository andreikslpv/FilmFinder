package com.andreikslpv.filmfinder.data.datasource.local

import androidx.lifecycle.LiveData
import com.andreikslpv.filmfinder.data.datasource.local.dao.FilmDao
import com.andreikslpv.filmfinder.data.datasource.local.db.RoomConstants
import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel
import javax.inject.Inject

class RoomLocalDataSource @Inject constructor(private val filmDao: FilmDao) : FilmsLocalDataSource {

    override fun getFavoritesFilms(): LiveData<List<FilmLocalModel>> {
        return filmDao.getLocalFilms(RoomConstants.COLUMN_IS_FAVORITE)
    }

    override fun getWatchLaterFilms(): LiveData<List<FilmLocalModel>> {
        return filmDao.getLocalFilms(RoomConstants.COLUMN_IS_WATCH_LATER)
    }

    override fun getFilmLocalState(film: FilmLocalModel): FilmLocalModel {
        return filmDao.getFilmById(film.id)
    }

    override fun saveFilm(film: FilmLocalModel) {
        filmDao.insertFilm(film)
    }
}