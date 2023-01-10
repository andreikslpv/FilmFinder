package com.andreikslpv.filmfinder.di.modules

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.usecase.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideChangeFilmLocalStateUseCase(filmsRepository: FilmsRepository): ChangeFilmLocalStateUseCase {
        return ChangeFilmLocalStateUseCase(filmsRepository)
    }

    @Provides
    @Singleton
    fun provideGetFavoritesFilmsUseCase(filmsRepository: FilmsRepository): GetFavoritesFilmsUseCase {
        return GetFavoritesFilmsUseCase(filmsRepository)
    }

    @Provides
    @Singleton
    fun provideGetFilmLocalStateUseCase(filmsRepository: FilmsRepository): GetFilmLocalStateUseCase {
        return GetFilmLocalStateUseCase(filmsRepository)
    }

    @Provides
    @Singleton
    fun provideGetPagedSearchResultUseCase(filmsRepository: FilmsRepository): GetPagedSearchResultUseCase {
        return GetPagedSearchResultUseCase(filmsRepository)
    }

    @Provides
    @Singleton
    fun provideGetSearchResultUseCase(): GetSearchResultUseCase {
        return GetSearchResultUseCase()
    }

    @Provides
    @Singleton
    fun provideGetWatchLaterFilmsUseCase(filmsRepository: FilmsRepository): GetWatchLaterFilmsUseCase {
        return GetWatchLaterFilmsUseCase(filmsRepository)
    }

    @Provides
    @Singleton
    fun provideGetPagedFilmsByCategoryUseCase(filmsRepository: FilmsRepository): GetPagedFilmsByCategoryUseCase {
        return GetPagedFilmsByCategoryUseCase(filmsRepository)
    }

    @Provides
    @Singleton
    fun provideGetAvailableCategories(filmsRepository: FilmsRepository): GetAvailableCategories {
        return GetAvailableCategories(filmsRepository)
    }
}