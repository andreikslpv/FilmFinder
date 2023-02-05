package com.andreikslpv.filmfinder.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.andreikslpv.filmfinder.data.datasource.api.ApiCallback
import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.data.datasource.api.imdb.ImdbDataSource
import com.andreikslpv.filmfinder.data.datasource.api.tmdb.TmdbDataSource
import com.andreikslpv.filmfinder.data.datasource.cache.FilmsCacheDataSource
import com.andreikslpv.filmfinder.data.datasource.local.DomainToLocalMapper
import com.andreikslpv.filmfinder.data.datasource.local.FilmsLocalDataSource
import com.andreikslpv.filmfinder.data.datasource.local.LocalToDomainListMapper
import com.andreikslpv.filmfinder.data.datasource.local.LocalToDomainMapper
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

const val PAGE_SIZE = 10

@Singleton
class FilmsRepositoryImpl @Inject constructor(
    private val localDataSource: FilmsLocalDataSource,
    private val cacheDataSource: FilmsCacheDataSource,
) : FilmsRepository {
    private var isNetworkAvailable = true
    private var currentCacheMode = ValuesType.AUTO
    private lateinit var apiDataSource: FilmsApiDataSource
    private var isApiDataSourceInitialized = false

    @Inject
    lateinit var imdbDataSource: Lazy<ImdbDataSource>

    @Inject
    lateinit var tmdbDataSource: Lazy<TmdbDataSource>


    override fun getPagedFilmsByCategory(category: CategoryType): Flow<PagingData<FilmDomainModel>> {
        return getFlowFromPagingSource(
            if (getResultOfChoiceSource()) apiDataSource.getFilmsByCategoryPagingSource(
                category,
                object : ApiCallback {
                    override fun onSuccess(films: List<FilmDomainModel>, currentIndex: Int) {
                        if (isNetworkAvailable) {
                            cacheDataSource.putCategoryToCache(
                                getCurrentApiDataSource(),
                                category,
                                films,
                                currentIndex,
                            )
                        }
                    }

                    override fun onFailure() {
                    }
                })
            else cacheDataSource.getFilmsByCategoryPagingSource(getCurrentApiDataSource(), category)
        )
    }

    override fun getPagedSearchResult(query: String): Flow<PagingData<FilmDomainModel>> {
        return getFlowFromPagingSource(
            if (getResultOfChoiceSource()) apiDataSource.getSearchResultPagingSource(query)
            else cacheDataSource.getSearchResultPagingSource(apiDataSource::checkComplianceApi, query)
        )
    }

    private fun getResultOfChoiceSource(): Boolean {
        // возвращает true если данные надо брать из апи, false - если из кеша
        return when (currentCacheMode) {
            ValuesType.AUTO -> isNetworkAvailable
            ValuesType.ALWAYS -> false
            ValuesType.NEVER -> true
            else -> false
        }
    }

    private fun getFlowFromPagingSource(pagingSource: PagingSource<Int, FilmDomainModel>): Flow<PagingData<FilmDomainModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                pagingSource
            }).flow
    }

    override fun getAvailableCategories(): List<CategoryType> {
        return apiDataSource.getAvailableCategories()
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

    override fun setCacheMode(mode: ValuesType) {
        when (mode) {
            ValuesType.AUTO, ValuesType.ALWAYS, ValuesType.NEVER -> currentCacheMode = mode
            else -> {}
        }
    }

    override fun deleteAllCachedFilms() {
        cacheDataSource.deleteCache()
    }

    override fun changeNetworkAvailability(newStatus: Boolean) {
        isNetworkAvailable = newStatus
    }

    // --------------- work with local

    override fun getWatchLaterFilms(): LiveData<List<FilmDomainModel>> {
        val result: LiveData<List<FilmDomainModel>> = Transformations.distinctUntilChanged(
            Transformations.map(localDataSource.getWatchLaterFilms()) {
                LocalToDomainListMapper.map(it)
            }
        )
        return result
    }

    override fun getFavoritesFilms(): LiveData<List<FilmDomainModel>> {
        val result: LiveData<List<FilmDomainModel>> = Transformations.distinctUntilChanged(
            Transformations.map(localDataSource.getFavoritesFilms()) {
                LocalToDomainListMapper.map(it)
            }
        )
        return result
    }

    override fun getFilmLocalState(filmId: String): LiveData<FilmDomainModel> {
        val result: LiveData<FilmDomainModel> = Transformations.distinctUntilChanged(
            Transformations.map(localDataSource.getFilmLocalState(filmId)) {
                LocalToDomainMapper.map(it)
            }
        )
        return result
    }

    override fun saveFilmToLocal(film: FilmDomainModel) {
        localDataSource.saveFilm(DomainToLocalMapper.map(film))
    }

}