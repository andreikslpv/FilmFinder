package com.andreikslpv.filmfinder.di.modules

import android.content.Context
import com.andreikslpv.filmfinder.data.datasource.local.db.DatabaseHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)
}