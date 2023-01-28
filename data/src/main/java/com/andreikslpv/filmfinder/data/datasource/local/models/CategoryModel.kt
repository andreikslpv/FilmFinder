package com.andreikslpv.filmfinder.data.datasource.local.models

import androidx.room.*
import com.andreikslpv.filmfinder.data.datasource.local.db.RoomConstants

@Entity(
    tableName = RoomConstants.TABLE_CACHED_CATEGORY,
    primaryKeys = [RoomConstants.COLUMN_API,
        RoomConstants.COLUMN_CATEGORY,
        RoomConstants.COLUMN_RANK],
    foreignKeys = [ForeignKey(
        entity = FilmLocalModel::class,
        parentColumns = [RoomConstants.COLUMN_FILM_ID],
        childColumns = [RoomConstants.COLUMN_FILM],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CategoryModel(
    @ColumnInfo(name = RoomConstants.COLUMN_API) val api: String,
    @ColumnInfo(name = RoomConstants.COLUMN_CATEGORY) val category: String,
    @ColumnInfo(name = RoomConstants.COLUMN_RANK) val rank: Int,
    @ColumnInfo(name = RoomConstants.COLUMN_FILM) val filmId: String,
)
