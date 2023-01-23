package com.andreikslpv.filmfinder.data.datasource.local.models

import androidx.room.*
import com.andreikslpv.filmfinder.data.datasource.local.db.DbConstants

@Entity(
    tableName = DbConstants.TABLE_CACHED_CATEGORY, foreignKeys = [ForeignKey(
        entity = FilmLocalModel::class,
        parentColumns = [DbConstants.COLUMN_FILM_ID],
        childColumns = [DbConstants.COLUMN_FILM],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = [DbConstants.COLUMN_API, DbConstants.COLUMN_CATEGORY, DbConstants.COLUMN_FILM],
        unique = false
    )]
)
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DbConstants.COLUMN_CATEGORY_ID) val id: Int = 0,
    @ColumnInfo(name = DbConstants.COLUMN_API) val api: String,
    @ColumnInfo(name = DbConstants.COLUMN_CATEGORY) val category: String,
    @ColumnInfo(name = DbConstants.COLUMN_FILM) val filmId: String,
)
