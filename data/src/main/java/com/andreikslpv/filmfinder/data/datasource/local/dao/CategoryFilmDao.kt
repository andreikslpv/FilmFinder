package com.andreikslpv.filmfinder.data.datasource.local.dao

import androidx.room.*
import com.andreikslpv.filmfinder.data.datasource.local.LocalToCategoryMapper
import com.andreikslpv.filmfinder.data.datasource.local.db.RoomConstants
import com.andreikslpv.filmfinder.data.datasource.local.models.CategoryModel
import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel

@Dao
abstract class CategoryFilmDao {

    @Query(
        "DELETE FROM ${RoomConstants.TABLE_CACHED_CATEGORY} "
                + "WHERE ${RoomConstants.COLUMN_API} = :api AND ${RoomConstants.COLUMN_CATEGORY} = :category"
    )
    abstract fun deleteCategory(api: String, category: String): Int

    @Insert(entity = FilmLocalModel::class, onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertFilms(films: List<FilmLocalModel>): List<Long>

    @Insert(entity = CategoryModel::class, onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCategory(list: List<CategoryModel>): List<Long>

    @Transaction
    open fun putCategoryToCache(api: String, category: String, films: List<FilmLocalModel>) {
        deleteCategory(api, category)
        insertFilms(films)
        insertCategory(LocalToCategoryMapper.map(api, category, films))
    }

    @Query(
        "DELETE FROM ${RoomConstants.TABLE_CACHED_FILMS} "
                + "WHERE ${RoomConstants.COLUMN_IS_FAVORITE} = false AND ${RoomConstants.COLUMN_IS_WATCH_LATER} = false"
    )
    abstract fun deleteCachedFilms(): Int

    @Query("DELETE FROM ${RoomConstants.TABLE_CACHED_CATEGORY}")
    abstract fun deleteAllCategory(): Int

    @Transaction
    open fun deleteCache() {
        deleteCachedFilms()
        deleteAllCategory()
    }
}