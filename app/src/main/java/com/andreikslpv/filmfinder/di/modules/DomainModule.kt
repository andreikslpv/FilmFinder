package com.andreikslpv.filmfinder.di.modules

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.SettingsRepository
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
    fun provideGetAvailableCategories(filmsRepository: FilmsRepository): GetAvailableCategoriesUseCase {
        return GetAvailableCategoriesUseCase(filmsRepository)
    }

    @Provides
    @Singleton
    fun provideSetApiDataSourceUseCase(filmsRepository: FilmsRepository): SetApiDataSourceUseCase {
        return SetApiDataSourceUseCase(filmsRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllSettingValue(settingsRepository: SettingsRepository): GetAllSettingValueUseCase {
        return GetAllSettingValueUseCase(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideGetSettingValue(settingsRepository: SettingsRepository): GetSettingValueUseCase {
        return GetSettingValueUseCase(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideSetSettingValue(settingsRepository: SettingsRepository): SetSettingValueUseCase {
        return SetSettingValueUseCase(settingsRepository)
    }
}