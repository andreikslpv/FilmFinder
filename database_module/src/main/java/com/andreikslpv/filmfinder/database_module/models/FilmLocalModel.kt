package com.andreikslpv.filmfinder.database_module.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.andreikslpv.filmfinder.database_module.db.RoomConstants

@Entity(
    tableName = RoomConstants.TABLE_CACHED_FILMS,
    indices = [Index(value = [RoomConstants.COLUMN_FILM_ID], unique = true)]
)
data class FilmLocalModel(
    @PrimaryKey
    @ColumnInfo(name = RoomConstants.COLUMN_FILM_ID) val id: String,
    @ColumnInfo(name = RoomConstants.COLUMN_TITLE) val title: String,
    @ColumnInfo(name = RoomConstants.COLUMN_POSTER_PREVIEW) val posterPreview: String,
    @ColumnInfo(name = RoomConstants.COLUMN_POSTER_DETAILS) val posterDetails: String,
    @ColumnInfo(name = RoomConstants.COLUMN_DESCRIPTION) val description: String,
    @ColumnInfo(name = RoomConstants.COLUMN_RATING) var rating: Double = 0.0,
    @ColumnInfo(name = RoomConstants.COLUMN_IS_FAVORITE) var isFavorite: Boolean = false,
    @ColumnInfo(name = RoomConstants.COLUMN_IS_WATCH_LATER) var isWatchLater: Boolean = false,
)