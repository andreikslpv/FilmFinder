package com.andreikslpv.filmfinder.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andreikslpv.filmfinder.data.datasource.local.db.RoomConstants
import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel
import io.reactivex.rxjava3.core.Observable

@Dao
interface FilmDao {

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_FILMS} WHERE ${RoomConstants.COLUMN_TITLE} LIKE :query")
    suspend fun searchFilmByName(query: String): List<FilmLocalModel>

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_FILMS} WHERE ${RoomConstants.COLUMN_IS_FAVORITE} = 1")
    fun getFavoritesFilms(): Observable<List<FilmLocalModel>>

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_FILMS} WHERE ${RoomConstants.COLUMN_IS_WATCH_LATER} = 1")
    fun getWatchLaterFilms(): Observable<List<FilmLocalModel>>

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_FILMS} WHERE ${RoomConstants.COLUMN_FILM_ID} = :id")
    fun getFilmById(id: String): Observable<FilmLocalModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilm(film: FilmLocalModel)

}