package com.andreikslpv.filmfinder.database_module

import com.andreikslpv.filmfinder.database_module.dao.CategoryDao
import com.andreikslpv.filmfinder.database_module.dao.CategoryFilmDao
import com.andreikslpv.filmfinder.database_module.dao.FilmDao

interface DatabaseProvider {

    fun provideFilmDao(): FilmDao
    fun provideCategoryDao(): CategoryDao
    fun provideCategoryFilmDao(): CategoryFilmDao

}