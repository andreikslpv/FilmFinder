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

    override fun getAd(): List<FilmsLocalModel> {
        return ApiToLocalMapper.map(apiDataSource.getAdFilms())
    }

    override fun changeFilmsState(film: FilmsLocalModel) {
        return localDataSource.changeItem(film)
    }

    override fun saveFilm(film: FilmsLocalModel): Boolean {
        return localDataSource.saveItem(film)
    }

    override fun removeFilm(film: FilmsLocalModel): Boolean {
        return localDataSource.removeItem(film)
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

    override fun getAllFilmsWithFavoritesChecked(): List<FilmsLocalModel> {
        val favoritesFilms = localDataSource.getItems()
        val allFilms = getAllFilms()

        allFilms.map { allFilm ->
            val equalsId = favoritesFilms.filter { favFilm ->
                allFilm.id == favFilm.id
            }

            if (equalsId.isNotEmpty()) {
                allFilm.isFavorite = equalsId[0].isFavorite
                allFilm.isWatchLater = equalsId[0].isWatchLater
            }
        }

        return allFilms
    }
}