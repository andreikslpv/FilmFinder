package com.andreikslpv.filmfinder.database_module.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andreikslpv.filmfinder.database_module.dao.CategoryDao
import com.andreikslpv.filmfinder.database_module.dao.CategoryFilmDao
import com.andreikslpv.filmfinder.database_module.dao.FilmDao
import com.andreikslpv.filmfinder.database_module.models.CategoryModel
import com.andreikslpv.filmfinder.database_module.models.FilmLocalModel

@Database(entities = [FilmLocalModel::class, CategoryModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun filmDao(): FilmDao

    abstract fun categoryDao(): CategoryDao

    abstract fun categoryFilmDao(): CategoryFilmDao
}