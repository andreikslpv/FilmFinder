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
}