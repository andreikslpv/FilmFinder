package com.andreikslpv.filmfinder.di.modules

import android.content.Context
import androidx.room.Room
import com.andreikslpv.filmfinder.data.datasource.local.db.AppDatabase
import com.andreikslpv.filmfinder.data.datasource.local.db.RoomConstants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            RoomConstants.DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideFilmDao(appDatabase: AppDatabase) = appDatabase.filmDao()

    @Singleton
    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase) = appDatabase.categoryDao()

    @Singleton
    @Provides
    fun provideCategoryFilmDao(appDatabase: AppDatabase) = appDatabase.categoryFilmDao()
}