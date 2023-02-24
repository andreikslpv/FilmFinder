package com.andreikslpv.filmfinder.data.datasource.local

import com.andreikslpv.filmfinder.data.datasource.local.dao.FilmDao
import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomLocalDataSource @Inject constructor(private val filmDao: FilmDao) : FilmsLocalDataSource {

    override fun getFavoritesFilms(): Flow<List<FilmLocalModel>> {
        return filmDao.getFavoritesFilms()
    }

    override fun getWatchLaterFilms(): Flow<List<FilmLocalModel>> {
        return filmDao.getWatchLaterFilms()
    }

    override fun getFilmLocalState(filmId: String): Flow<FilmLocalModel> {
        return filmDao.getFilmById(filmId)
    }

    override fun saveFilm(film: FilmLocalModel) {
        filmDao.insertFilm(film)
    }
}