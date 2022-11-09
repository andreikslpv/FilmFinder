package com.andreikslpv.filmfinder.data.repository

import com.andreikslpv.filmfinder.data.datasource.FilmsApiDataSource
import com.andreikslpv.filmfinder.data.datasource.FilmsCacheDataSource
import com.andreikslpv.filmfinder.data.datasource.FilmsLocalDataSource
import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel
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

    override fun getAllLocalSavedFilms(): List<FilmsLocalModel> {
        return localDataSource.getItems()
    }

    override fun saveFilms(listFilmsToSave: List<FilmsLocalModel>) {
        localDataSource.saveItems(listFilmsToSave)
    }
}