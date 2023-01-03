package com.andreikslpv.filmfinder.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.data.datasource.api.TmdbFilmsByCategoryDataSource
import com.andreikslpv.filmfinder.data.datasource.api.services.TmdbCategoryService
import com.andreikslpv.filmfinder.data.datasource.local.FilmsLocalDataSource
import com.andreikslpv.filmfinder.domain.ApiCallback
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import kotlinx.coroutines.flow.Flow

class FilmsRepositoryImpl(
    private val apiDataSource: FilmsApiDataSource,
    private val localDataSource: FilmsLocalDataSource,
    private val categoryService: TmdbCategoryService
) : FilmsRepository {

    override fun getFilmsFromApi(page: Int, language: String, callback: ApiCallback) {
        apiDataSource.getFilmsFromApi(page, language, callback)
    }

    override fun getSearchResultFromApi(
        query: String,
        page: Int,
        language: String,
        callback: ApiCallback
    ) {
        apiDataSource.getSearchResultFromApi(query, page, language, callback)
    }

    override fun getPagedFilmsByCategory(language: String): Flow<PagingData<FilmDomainModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { TmdbFilmsByCategoryDataSource(categoryService, language) }).flow
    }

    override fun getAllLocalSavedFilms(): List<FilmDomainModel> {
        return LocalToDomainMapper.map(localDataSource.getItems())
    }

    override fun saveFilmToLocal(film: FilmDomainModel) {
        localDataSource.saveItem(DomainToLocalMapper.map(film))
    }
}