package com.andreikslpv.filmfinder.data.repository

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
                    override fun onSuccess(films: List<FilmDomainModel>) {
                        if (isNetworkAvailable) {
                            cacheDataSource.putCategoryToCache(
                                getCurrentApiDataSource(),
                                category,
                                films,
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
            else cacheDataSource.getSearchResultPagingSource(getCurrentApiDataSource(), query)
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

    override fun getAllLocalSavedFilms(): List<FilmDomainModel> {
        return LocalToDomainListMapper.map(localDataSource.getItems())
    }

    override fun saveFilmToLocal(film: FilmDomainModel) {
        localDataSource.saveItem(DomainToLocalMapper.map(film))
    }


}