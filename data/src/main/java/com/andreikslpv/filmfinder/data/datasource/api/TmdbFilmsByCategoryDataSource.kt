package com.andreikslpv.filmfinder.data.datasource.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andreikslpv.filmfinder.data.datasource.api.services.TmdbCategoryService
import com.andreikslpv.filmfinder.data.datasource.api.utils.TmdbConstants
import com.andreikslpv.filmfinder.data.datasource.api.utils.TmdbKey
import com.andreikslpv.filmfinder.data.datasource.api.utils.TmdbToDomainModel
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import retrofit2.HttpException

class TmdbFilmsByCategoryDataSource(
    private val categoryService: TmdbCategoryService,
    private val language: String
) : PagingSource<Int, FilmDomainModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmDomainModel> {
        try {
            var pageNumber = params.key ?: TmdbConstants.START_PAGE
            if (pageNumber == 0) pageNumber = 1
            val response = categoryService.getFilms(
                TmdbConstants.VERSION_API,
                TmdbConstants.PATH_GET_POPULAR_1,
                TmdbConstants.PATH_GET_POPULAR_2,
                TmdbKey.KEY,
                language,
                pageNumber
            )

            return if (response.isSuccessful) {
                LoadResult.Page(
                    data = TmdbToDomainModel.map(response.body()!!.results),
                    prevKey = if (pageNumber > 1) pageNumber - 1 else null,
                    nextKey = if (pageNumber < response.body()!!.totalPages) pageNumber + 1 else null
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
        TODO("Not yet implemented")
    }
}