package com.andreikslpv.filmfinder.repository

import com.andreikslpv.filmfinder.datasource.FilmsApiDataSource
import com.andreikslpv.filmfinder.datasource.FilmsCacheDataSource
import com.andreikslpv.filmfinder.datasource.FilmsLocalDataSource
import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel
import com.andreikslpv.filmfinder.domain.FilmsRepository

class FilmsRepositoryImpl(
    private val cacheDataSource: FilmsCacheDataSource,
    private val apiDataSource: FilmsApiDataSource,
    private val localDataSource: FilmsLocalDataSource
) : FilmsRepository {

    override fun getAllFilms(): List<FilmsLocalModel> {
        val allFilms: List<FilmsLocalModel> = cacheDataSource.films
        // если в кэш уже загружен список фильмов, то возвращаем его
        if (allFilms.isNotEmpty()) return allFilms
        // иначе получаем из сети
        cacheDataSource.films = ApiToLocalMapper.map(apiDataSource.getAllFilms())
        return cacheDataSource.films.ifEmpty { emptyList() }
    }

    override fun changeFilmLocalState(film: FilmsLocalModel) {
        return localDataSource.changeItem(film)
    }

    override fun getFilmLocalState(film: FilmsLocalModel): FilmsLocalModel {
        val localFilm = localDataSource.getItemById(film.id)
        if (localFilm != null) {
            film.isFavorite = localFilm.isFavorite
            film.isWatchLater = localFilm.isWatchLater
        }
        return film
    }

    override fun getFavoriteFilms(): List<FilmsLocalModel> {
        return localDataSource.getItems().filter {
            it.isFavorite
        }
    }

    override fun getWatchLaterFilms(): List<FilmsLocalModel> {
        return localDataSource.getItems().filter {
            it.isWatchLater
        }
    }

}