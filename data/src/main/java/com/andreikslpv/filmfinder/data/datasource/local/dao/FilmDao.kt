package com.andreikslpv.filmfinder.data.datasource.local.dao

import androidx.room.*
import com.andreikslpv.filmfinder.data.datasource.local.db.RoomConstants
import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel

@Dao
interface FilmDao {

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_FILMS} WHERE ${RoomConstants.COLUMN_TITLE} LIKE :query")
    suspend fun searchFilmByName(query: String): List<FilmLocalModel>

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_FILMS} WHERE ${RoomConstants.COLUMN_IS_FAVORITE} = true")
    fun getFavoriteFilms(): List<FilmLocalModel>

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_FILMS} WHERE ${RoomConstants.COLUMN_IS_WATCH_LATER} = true")
    fun getWatchLaterFilms(): List<FilmLocalModel>

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_FILMS} WHERE ${RoomConstants.COLUMN_FILM_ID} = :id")
    fun getFilmById(id: String): FilmLocalModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilmList(list: List<FilmLocalModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilm(film: FilmLocalModel)

    @Update
    fun updateFilm(film: FilmLocalModel)
}