package com.andreikslpv.filmfinder.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.andreikslpv.filmfinder.data.datasource.local.db.DbConstants
import com.andreikslpv.filmfinder.data.datasource.local.models.CategoryAndFilmModel

@Dao
interface CategoryDao {
    @Transaction
    @Query(
        "SELECT * FROM ${DbConstants.TABLE_CACHED_CATEGORY} "
                + "WHERE ${DbConstants.COLUMN_API} = :api AND ${DbConstants.COLUMN_CATEGORY} = :category"
    )
    fun getCategoryWithFilms(api: String, category: String): List<CategoryAndFilmModel>
}