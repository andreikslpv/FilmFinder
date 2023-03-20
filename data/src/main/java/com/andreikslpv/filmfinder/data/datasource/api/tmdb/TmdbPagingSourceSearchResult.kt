package com.andreikslpv.filmfinder.data.datasource.api.tmdb

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbDtoResults
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbServiceSearchResult
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbToDomainModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class TmdbPagingSourceSearchResult @Inject constructor(
    private val service: TmdbServiceSearchResult,
    private val language: String,
    private val query: String
) : RxPagingSource<Int, FilmDomainModel>() {
    private val step = 1

    override fun getRefreshKey(state: PagingState<Int, FilmDomainModel>): Int? {
        // Самый последний доступный индекс в списке
        val anchorPosition = state.anchorPosition ?: return null
        // преобразуем item index в page index
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        // page не имеет значения "текущее", поэтому вычисляем сами
        return page.prevKey?.plus(step) ?: page.nextKey?.minus(step)
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, FilmDomainModel>> {
        var pageNumber = params.key ?: TmdbConstants.START_PAGE
        if (pageNumber == 0) pageNumber = TmdbConstants.START_PAGE
        return service.getFilms(
            query = query,
            language = language,
            page = pageNumber
        )
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it, pageNumber) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(
        data: TmdbDtoResults,
        pageNumber: Int
    ): LoadResult<Int, FilmDomainModel> {
        return LoadResult.Page(
            data = TmdbToDomainModel.map(data.results),
            prevKey = if (pageNumber > TmdbConstants.START_PAGE) pageNumber - step else null,
            nextKey = if (pageNumber < data.totalPages) pageNumber + step else null
        )
    }

}