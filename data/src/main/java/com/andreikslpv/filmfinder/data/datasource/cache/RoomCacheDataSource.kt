package com.andreikslpv.filmfinder.data.datasource.cache

import androidx.paging.PagingSource
import com.andreikslpv.filmfinder.data.datasource.local.DomainToLocalListMapper
import com.andreikslpv.filmfinder.data.datasource.local.dao.CategoryDao
import com.andreikslpv.filmfinder.data.datasource.local.dao.CategoryFilmDao
import com.andreikslpv.filmfinder.data.datasource.local.dao.FilmDao
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomCacheDataSource @Inject constructor(
    private val filmDao: FilmDao,
    private val categoryDao: CategoryDao,
    private val categoryFilmDao: CategoryFilmDao,
) : FilmsCacheDataSource {

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun putCategoryToCache(
        api: ValuesType,
        category: CategoryType,
        films: List<FilmDomainModel>,
        currentIndex: Int
    ) {
        scope.launch {
            categoryFilmDao.putCategoryToCache(
                api.value,
                category.name,
                DomainToLocalListMapper.map(films),
                currentIndex,
            )
        }
    }

    override fun deleteCache() {
        scope.launch {
            categoryFilmDao.deleteCache()
        }
    }

    override fun getFilmsByCategoryPagingSource(
        api: ValuesType,
        category: CategoryType
    ): PagingSource<Int, FilmDomainModel> {
        return RoomPagingSourceFilmsByCategory(categoryDao, api.value, category.name)
    }

    override fun getSearchResultPagingSource(
        function: (string: String) -> Boolean,
        query: String
    ): PagingSource<Int, FilmDomainModel> {
        return RoomPagingSourceSearchResult(filmDao, function, query)
    }
}