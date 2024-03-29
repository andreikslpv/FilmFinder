package com.andreikslpv.filmfinder.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.andreikslpv.filmfinder.data.datasource.api.ApiCallback
import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.data.datasource.cache.FilmsCacheDataSource
import com.andreikslpv.filmfinder.data.datasource.local.DomainToLocalMapper
import com.andreikslpv.filmfinder.data.datasource.local.FilmsLocalDataSource
import com.andreikslpv.filmfinder.data.datasource.local.LocalToDomainListMapper
import com.andreikslpv.filmfinder.data.datasource.local.LocalToDomainMapper
import com.andreikslpv.filmfinder.data.di.ImdbSourceQualifier
import com.andreikslpv.filmfinder.data.di.TmdbSourceQualifier
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.EmptyCoroutineContext

const val PAGE_SIZE = 10

@Singleton
class FilmsRepositoryImpl @Inject constructor(
    private val localDataSource: FilmsLocalDataSource,
    private val cacheDataSource: FilmsCacheDataSource,
    @ImdbSourceQualifier private val imdbDataSource: FilmsApiDataSource,
    @TmdbSourceQualifier private val tmdbDataSource: FilmsApiDataSource,
) : FilmsRepository {
    private var currentApi = ValuesType.NONE
    private val _currentCategoryList = MutableStateFlow(emptyList<CategoryType>())

    private var isApiAvailable = true
    private var currentCacheMode = ValuesType.AUTO
    private lateinit var apiDataSource: FilmsApiDataSource

    override fun getPagedFilmsByCategory(category: CategoryType): Flowable<PagingData<FilmDomainModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                if (getResultOfChoiceSource()) apiDataSource.getFilmsByCategoryPagingSource(
                    category,
                    object : ApiCallback {
                        override fun onSuccess(
                            films: List<FilmDomainModel>,
                            currentIndex: Int
                        ) {
                            if (isApiAvailable) {
                                Completable.fromSingle<Nothing> {
                                    cacheDataSource.putCategoryToCache(
                                        apiDataSource.getApiType(),
                                        category,
                                        films,
                                        currentIndex,
                                    )
                                }
                                    .subscribeOn(Schedulers.io())
                                    .subscribe()
                            }
                        }

                        override fun onFailure() {
                        }
                    })
                else {
                    // загружаем данные из кэша и меняем статус доступности апи на true,
                    // чтобы в следующий раз в режиме авто снова сначала была попытка получить данные из апи
                    isApiAvailable = true
                    cacheDataSource.getFilmsByCategoryPagingSource(
                        apiDataSource.getApiType(),
                        category
                    )
                }
            }).flowable
    }

    override fun getPagedSearchResult(query: String): Flowable<PagingData<FilmDomainModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                if (getResultOfChoiceSource()) apiDataSource.getSearchResultPagingSource(query)
                else {
                    // загружаем данные из кэша и меняем статус доступности апи на true,
                    // чтобы в следующий раз в режиме авто снова сначала была попытка получить данные из апи
                    isApiAvailable = true
                    cacheDataSource.getSearchResultPagingSource(
                        apiDataSource::checkComplianceApi,
                        query
                    )
                }
            }).flowable
    }

    override fun getFilmByIdFromApi(filmId: String): Single<List<FilmDomainModel>> {
        return imdbDataSource.getFilmByIdFromApi(filmId)
    }

    private fun getResultOfChoiceSource(): Boolean {
        // возвращает true если данные надо брать из апи, false - если из кеша
        return when (currentCacheMode) {
            ValuesType.AUTO -> isApiAvailable
            ValuesType.ALWAYS -> false
            ValuesType.NEVER -> true
            else -> false
        }
    }

    override fun getAvailableCategories(): MutableStateFlow<List<CategoryType>> {
        return _currentCategoryList
    }

    override fun setApiDataSource(api: ValuesType) {
        when (api) {
            ValuesType.TMDB -> {
                apiDataSource = tmdbDataSource
                emitWhenApiChanged()
            }
            ValuesType.IMDB -> {
                apiDataSource = imdbDataSource
                emitWhenApiChanged()
            }
            else -> {}
        }
    }

    private fun emitWhenApiChanged() {
        currentApi = apiDataSource.getApiType()
        CoroutineScope(EmptyCoroutineContext).launch {
            _currentCategoryList.tryEmit(apiDataSource.getAvailableCategories())
        }
    }

    override fun getCurrentApiDataSource(): Observable<ValuesType> {
        return Observable.create {
            it.onNext(currentApi)
        }
            .distinctUntilChanged()
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

    override fun changeApiAvailability(newStatus: Boolean) {
        isApiAvailable = newStatus
    }

    // --------------- work with local

    override fun getWatchLaterFilms(): Observable<List<FilmDomainModel>> {
        return localDataSource.getWatchLaterFilms()
            .map { LocalToDomainListMapper.map(it) }
    }

    override fun getFavoritesFilms(): Observable<List<FilmDomainModel>> {
        return localDataSource.getFavoritesFilms()
            .map { LocalToDomainListMapper.map(it) }
    }

    override fun getFilmLocalState(filmId: String): Observable<FilmDomainModel> {
        return localDataSource.getFilmLocalState(filmId)
            .map { LocalToDomainMapper.map(it) }
    }

    override fun saveFilmToLocal(film: FilmDomainModel, replace: Boolean) {
        localDataSource.saveFilm(DomainToLocalMapper.map(film), replace)
    }

}