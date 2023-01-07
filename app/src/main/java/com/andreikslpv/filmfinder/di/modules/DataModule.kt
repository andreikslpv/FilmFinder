package com.andreikslpv.filmfinder.di.modules

import android.content.Context
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.data.datasource.api.tmdb.TmdbDataSource
import com.andreikslpv.filmfinder.data.datasource.local.FilmsLocalDataSource
import com.andreikslpv.filmfinder.data.datasource.local.JsonDataSource
import com.andreikslpv.filmfinder.data.repository.FilmsRepositoryImpl
import com.andreikslpv.filmfinder.domain.FilmsRepository
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Singleton

const val NAME_OF_LOCAL_STORAGE = "local.json"

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideFilmsApiDataSource(context: Context): FilmsApiDataSource {
        return TmdbDataSource(context.getString(R.string.tmdb_language))
    }

    @Provides
    @Singleton
    fun provideFilmsLocalDataSource(context: Context): FilmsLocalDataSource {
        return JsonDataSource(File("${context.filesDir}/$NAME_OF_LOCAL_STORAGE"))
    }

    @Provides
    @Singleton
    fun provideFilmsRepository(
        filmsApiDataSource: FilmsApiDataSource,
        filmsLocalDataSource: FilmsLocalDataSource
    ): FilmsRepository {
        return FilmsRepositoryImpl(filmsApiDataSource, filmsLocalDataSource)
    }
}