package com.andreikslpv.filmfinder.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andreikslpv.filmfinder.data.datasource.local.dao.CategoryDao
import com.andreikslpv.filmfinder.data.datasource.local.dao.CategoryFilmDao
import com.andreikslpv.filmfinder.data.datasource.local.dao.FilmDao
import com.andreikslpv.filmfinder.data.datasource.local.models.CategoryModel
import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel

@Database(entities = [FilmLocalModel::class, CategoryModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun filmDao(): FilmDao

    abstract fun categoryDao(): CategoryDao

    abstract fun categoryFilmDao(): CategoryFilmDao
}