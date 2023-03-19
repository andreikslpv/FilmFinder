package com.andreikslpv.filmfinder.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.andreikslpv.filmfinder.data.datasource.local.db.RoomConstants
import com.andreikslpv.filmfinder.data.datasource.local.models.CategoryAndFilmModel

@Dao
interface CategoryDao {
    @Transaction
    @Query(
        "SELECT * FROM ${RoomConstants.TABLE_CACHED_CATEGORY}"
                + " WHERE ${RoomConstants.COLUMN_API} = :api AND ${RoomConstants.COLUMN_CATEGORY} = :category"
                + " ORDER BY ${RoomConstants.COLUMN_RANK}"
    )
    fun getCategoryWithFilms(api: String, category: String): List<CategoryAndFilmModel>
}