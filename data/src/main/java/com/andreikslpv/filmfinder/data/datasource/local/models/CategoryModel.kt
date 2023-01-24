package com.andreikslpv.filmfinder.data.datasource.local.models

import androidx.room.*
import com.andreikslpv.filmfinder.data.datasource.local.db.RoomConstants

@Entity(
    tableName = RoomConstants.TABLE_CACHED_CATEGORY, foreignKeys = [ForeignKey(
        entity = FilmLocalModel::class,
        parentColumns = [RoomConstants.COLUMN_FILM_ID],
        childColumns = [RoomConstants.COLUMN_FILM],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = [RoomConstants.COLUMN_API, RoomConstants.COLUMN_CATEGORY, RoomConstants.COLUMN_FILM],
        unique = false
    )]
)
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = RoomConstants.COLUMN_CATEGORY_ID) val id: Int = 0,
    @ColumnInfo(name = RoomConstants.COLUMN_API) val api: String,
    @ColumnInfo(name = RoomConstants.COLUMN_CATEGORY) val category: String,
    @ColumnInfo(name = RoomConstants.COLUMN_FILM) val filmId: String,
)
