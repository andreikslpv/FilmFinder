package com.andreikslpv.filmfinder.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.data.datasource.api.imdb.ImdbDataSource
import com.andreikslpv.filmfinder.data.datasource.api.tmdb.TmdbDataSource
import com.andreikslpv.filmfinder.data.datasource.local.DomainToLocalMapper
import com.andreikslpv.filmfinder.data.datasource.local.FilmsLocalDataSource
import com.andreikslpv.filmfinder.data.datasource.local.LocalToDomainMapper
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.ValuesType
import kotlinx.coroutines.flow.Flow
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton

const val PAGE_SIZE = 10

@Singleton
class FilmsRepositoryImpl @Inject constructor(
    private val localDataSource: FilmsLocalDataSource,
) : FilmsRepository {
    private lateinit var apiDataSource: FilmsApiDataSource
    private var isApiDataSourceInitialized = false

    @Inject
    lateinit var imdbDataSource: Lazy<ImdbDataSource>

    @Inject
    lateinit var tmdbDataSource: Lazy<TmdbDataSource>


    override fun getPagedFilmsByCategory(category: CategoryType): Flow<PagingData<FilmDomainModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                apiDataSource.getFilmsByCategoryPagingSource(category)
            }).flow
    }

    override fun getPagedSearchResult(query: String): Flow<PagingData<FilmDomainModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                apiDataSource.getSearchResultPagingSource(query)
            }).flow
    }

    override fun getAvailableCategories(): List<CategoryType> {
        return apiDataSource.getAvailableCategories()
    }

    override fun getAllLocalSavedFilms(): List<FilmDomainModel> {
        return LocalToDomainMapper.map(localDataSource.getItems())
    }

    override fun saveFilmToLocal(film: FilmDomainModel) {
        localDataSource.saveItem(DomainToLocalMapper.map(film))
    }

    override fun setApiDataSource(api: ValuesType) {
        when (api) {
            ValuesType.TMDB -> {
                apiDataSource = tmdbDataSource.get()
                isApiDataSourceInitialized = true
            }
            ValuesType.IMDB -> {
                apiDataSource = imdbDataSource.get()
                isApiDataSourceInitialized = true
            }
            else -> {}
        }
    }

    override fun getCurrentApiDataSource(): ValuesType {
        return if (isApiDataSourceInitialized) apiDataSource.getApiType()
        else ValuesType.NONE
    }
}