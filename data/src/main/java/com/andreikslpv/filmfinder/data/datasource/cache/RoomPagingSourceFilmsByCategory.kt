package com.andreikslpv.filmfinder.data.datasource.cache

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.andreikslpv.filmfinder.remote_module.imdb.ImdbConstants
import com.andreikslpv.filmfinder.data.datasource.local.CategoryAndFilmToDomainMapper
import com.andreikslpv.filmfinder.database_module.dao.CategoryDao
import com.andreikslpv.filmfinder.database_module.models.CategoryAndFilmModel
import com.andreikslpv.filmfinder.data.repository.PAGE_SIZE
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class RoomPagingSourceFilmsByCategory(
    private val categoryDao: CategoryDao,
    private val api: String,
    private val category: String,
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

        return Single.fromCallable {
            categoryDao.getCategoryWithFilms(api, category)
        }
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it, pageNumber) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(
        data: List<CategoryAndFilmModel>,
        pageNumber: Int
    ): LoadResult<Int, FilmDomainModel> {
        val totalPages: Int = data.size / PAGE_SIZE

        return LoadResult.Page(
            data = CategoryAndFilmToDomainMapper.map(data),
            prevKey = if (pageNumber > ImdbConstants.START_PAGE) pageNumber - step else null,
            nextKey = if (pageNumber < totalPages) pageNumber + step else null
        )
    }
}