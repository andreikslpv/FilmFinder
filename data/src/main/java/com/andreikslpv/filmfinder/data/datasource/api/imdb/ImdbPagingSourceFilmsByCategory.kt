package com.andreikslpv.filmfinder.data.datasource.api.imdb

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.andreikslpv.filmfinder.data.datasource.api.ApiCallback
import com.andreikslpv.filmfinder.data.repository.PAGE_SIZE
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.remote_module.imdb.ImdbCategoryItemToDomainModel
import com.andreikslpv.filmfinder.remote_module.imdb.ImdbDtoCategoryResults
import com.andreikslpv.filmfinder.remote_module.imdb.ImdbServiceFilmsByCategory
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class ImdbPagingSourceFilmsByCategory(
    private val service: ImdbServiceFilmsByCategory,
    private val language: String,
    private val category: String,
    private val callback: ApiCallback,
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
        val pageNumber = params.key ?: ImdbConstants.START_PAGE
        return service.getFilms(
            language = language,
            category = category
        )
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it, pageNumber) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(
        data: ImdbDtoCategoryResults,
        pageNumber: Int
    ): LoadResult<Int, FilmDomainModel> {
        val totalPages: Int = data.items!!.size / PAGE_SIZE

        return if (data.errorMessage.isNullOrEmpty()) {
            callback.onSuccess(ImdbCategoryItemToDomainModel.map(data.items), 0)
            LoadResult.Page(
                data = ImdbCategoryItemToDomainModel.map(data.items),
                prevKey = if (pageNumber > ImdbConstants.START_PAGE) pageNumber - step else null,
                nextKey = if (pageNumber < totalPages) pageNumber + step else null
            )
        } else LoadResult.Error(Exception(data.errorMessage))
    }
}