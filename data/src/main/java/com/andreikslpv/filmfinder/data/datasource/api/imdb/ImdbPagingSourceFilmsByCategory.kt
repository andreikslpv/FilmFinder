package com.andreikslpv.filmfinder.data.datasource.api.imdb

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andreikslpv.filmfinder.data.datasource.api.ApiCallback
import com.andreikslpv.filmfinder.data.repository.PAGE_SIZE
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import retrofit2.HttpException

class ImdbPagingSourceFilmsByCategory(
    private val categoryService: ImdbServiceFilmsByCategory,
    private val language: String,
    private val category: String,
    private val callback: ApiCallback,
) : PagingSource<Int, FilmDomainModel>() {
    private val step = 1

    override fun getRefreshKey(state: PagingState<Int, FilmDomainModel>): Int? {
        // Самый последний доступный индекс в списке
        val anchorPosition = state.anchorPosition ?: return null
        // преобразуем item index в page index
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        // page не имеет значения "текущее", поэтому вычисляем сами
        return page.prevKey?.plus(step) ?: page.nextKey?.minus(step)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmDomainModel> {
        try {
            val pageNumber = params.key ?: ImdbConstants.START_PAGE
            val response = categoryService.getFilms(
                language = language,
                category = category
            )

            return if (response.isSuccessful) {
                if (response.body()!!.errorMessage.isNullOrEmpty()) {
                    val totalPages: Int = response.body()!!.items!!.size / PAGE_SIZE
                    callback.onSuccess(ImdbCategoryItemToDomainModel.map(response.body()!!.items))
                    LoadResult.Page(
                        data = ImdbCategoryItemToDomainModel.map(response.body()!!.items),
                        prevKey = if (pageNumber > ImdbConstants.START_PAGE) pageNumber - step else null,
                        nextKey = if (pageNumber < totalPages) pageNumber + step else null
                    )
                } else {
                    LoadResult.Error(ImdbError.getError(response.body()!!.errorMessage))
                }
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}