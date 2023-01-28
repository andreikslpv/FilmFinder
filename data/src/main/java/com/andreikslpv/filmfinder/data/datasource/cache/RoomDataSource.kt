package com.andreikslpv.filmfinder.data.datasource.cache

import androidx.paging.PagingSource
import com.andreikslpv.filmfinder.data.datasource.local.DomainToLocalListMapper
import com.andreikslpv.filmfinder.data.datasource.local.dao.CategoryDao
import com.andreikslpv.filmfinder.data.datasource.local.dao.CategoryFilmDao
import com.andreikslpv.filmfinder.data.datasource.local.dao.FilmDao
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import java.util.concurrent.Executors
import javax.inject.Inject

class RoomDataSource @Inject constructor(
    private val filmDao: FilmDao,
    private val categoryDao: CategoryDao,
    private val categoryFilmDao: CategoryFilmDao,
) : FilmsCacheDataSource {

    override fun putCategoryToCache(
        api: ValuesType,
        category: CategoryType,
        films: List<FilmDomainModel>,
        currentIndex: Int
    ) {
        Executors.newSingleThreadExecutor().execute {
            categoryFilmDao.putCategoryToCache(
                api.value,
                category.name,
                DomainToLocalListMapper.map(films),
                currentIndex,
            )
        }
    }

    override fun deleteCache() {
        Executors.newSingleThreadExecutor().execute {
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
        api: ValuesType,
        query: String
    ): PagingSource<Int, FilmDomainModel> {
        return RoomPagingSourceSearchResult(filmDao, api.value, query)
    }
}