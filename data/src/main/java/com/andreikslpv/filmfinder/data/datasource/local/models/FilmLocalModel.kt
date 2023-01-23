package com.andreikslpv.filmfinder.data.datasource.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.andreikslpv.filmfinder.data.datasource.local.db.DbConstants

@Entity(
    tableName = DbConstants.TABLE_CACHED_FILMS,
    indices = [Index(value = [DbConstants.COLUMN_FILM_ID], unique = true)]
)
data class FilmLocalModel(
    @PrimaryKey
    @ColumnInfo(name = DbConstants.COLUMN_FILM_ID) val id: String,
    @ColumnInfo(name = DbConstants.COLUMN_TITLE) val title: String,
    @ColumnInfo(name = DbConstants.COLUMN_POSTER_PREVIEW) val posterPreview: String,
    @ColumnInfo(name = DbConstants.COLUMN_POSTER_DETAILS) val posterDetails: String,
    @ColumnInfo(name = DbConstants.COLUMN_DESCRIPTION) val description: String,
    @ColumnInfo(name = DbConstants.COLUMN_RATING) var rating: Double = 0.0,
    @ColumnInfo(name = DbConstants.COLUMN_IS_FAVORITE) var isFavorite: Boolean = false,
    @ColumnInfo(name = DbConstants.COLUMN_IS_WATCH_LATER) var isWatchLater: Boolean = false,
)