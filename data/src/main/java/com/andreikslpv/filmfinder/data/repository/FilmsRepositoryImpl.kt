package com.andreikslpv.filmfinder.data.repository

import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.data.datasource.local.FilmsLocalDataSource
import com.andreikslpv.filmfinder.domain.ApiCallback
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class FilmsRepositoryImpl(
    private val apiDataSource: FilmsApiDataSource,
    private val localDataSource: FilmsLocalDataSource
) : FilmsRepository {

    override fun getFilmsFromApi(page: Int, language: String, callback: ApiCallback) {
        apiDataSource.getFilmsFromApi(page, language, callback)
    }

    override fun getSearchResultFromApi(query: String, page: Int, language: String, callback: ApiCallback) {
        apiDataSource.getSearchResultFromApi(query, page, language, callback)
    }

    override fun getAllLocalSavedFilms(): List<FilmDomainModel> {
        return LocalToDomainMapper.map(localDataSource.getItems())
    }

    override fun saveFilmToLocal(film: FilmDomainModel) {
        localDataSource.saveItem(DomainToLocalMapper.map(film))
    }
}