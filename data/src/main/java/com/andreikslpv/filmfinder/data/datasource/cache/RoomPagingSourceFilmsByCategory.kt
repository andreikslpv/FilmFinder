package com.andreikslpv.filmfinder.data.datasource.cache

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andreikslpv.filmfinder.data.datasource.local.CategoryAndFilmToDomainMapper
import com.andreikslpv.filmfinder.data.datasource.local.dao.CategoryDao
import com.andreikslpv.filmfinder.data.repository.PAGE_SIZE
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class RoomPagingSourceFilmsByCategory(
    private val categoryDao: CategoryDao,
    private val api: String,
    private val category: String,
) : PagingSource<Int, FilmDomainModel>() {
    private val step = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmDomainModel> {
        return try {
            val pageNumber = params.key ?: START_PAGE
            //Создаем курсор на основании запроса "Получить из таблицы записи для указанных апи и категории"
            val films =
                CategoryAndFilmToDomainMapper.map(categoryDao.getCategoryWithFilms(api, category))
            val totalPages = films.size / PAGE_SIZE

            LoadResult.Page(
                data = films,
                prevKey = if (pageNumber > START_PAGE) pageNumber - step else null,
                nextKey = if (pageNumber < totalPages) pageNumber + step else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
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