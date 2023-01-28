package com.andreikslpv.filmfinder.data.datasource.api.tmdb

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andreikslpv.filmfinder.data.datasource.api.ApiCallback
import com.andreikslpv.filmfinder.data.repository.PAGE_SIZE
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import retrofit2.HttpException

class TmdbPagingSourceFilmsByCategory(
    private val categoryService: TmdbServiceFilmsByCategory,
    private val language: String,
    private val category: String,
    private val callback: ApiCallback,
) : PagingSource<Int, FilmDomainModel>() {
    private val step = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmDomainModel> {
        try {
            var pageNumber = params.key ?: TmdbConstants.START_PAGE
            if (pageNumber == 0) pageNumber = TmdbConstants.START_PAGE
            val response = categoryService.getFilms(
                path2 = category,
                language = language,
                page = pageNumber
            )

            return if (response.isSuccessful) {
                val currentIndex = (response.body()!!.page - TmdbConstants.START_PAGE) * PAGE_SIZE
                callback.onSuccess(TmdbToDomainModel.map(response.body()!!.results), currentIndex)
                LoadResult.Page(
                    data = TmdbToDomainModel.map(response.body()!!.results),
                    prevKey = if (pageNumber > TmdbConstants.START_PAGE) pageNumber - step else null,
                    nextKey = if (pageNumber < response.body()!!.totalPages) pageNumber + step else null
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FilmDomainModel>): Int? {
        // Самый последний доступный индекс в списке
        val anchorPosition = state.anchorPosition ?: return null
        // преобразуем item index в page index
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        // page не имеет значения "текущее", поэтому вычисляем сами
        return page.prevKey?.plus(step) ?: page.nextKey?.minus(step)
    }
}