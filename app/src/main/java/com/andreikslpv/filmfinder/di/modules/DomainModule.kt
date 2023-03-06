package com.andreikslpv.filmfinder.di.modules

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.SettingsRepository
import com.andreikslpv.filmfinder.domain.usecase.*
import com.andreikslpv.filmfinder.domain.usecase.apicache.*
import com.andreikslpv.filmfinder.domain.usecase.local.ChangeFilmLocalStateUseCase
import com.andreikslpv.filmfinder.domain.usecase.local.GetFavoritesFilmsUseCase
import com.andreikslpv.filmfinder.domain.usecase.local.GetFilmLocalStateUseCase
import com.andreikslpv.filmfinder.domain.usecase.local.GetWatchLaterFilmsUseCase
import com.andreikslpv.filmfinder.domain.usecase.management.*
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
//    @Singleton
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
    fun provideGetCurrentApiDataSourceUseCase(filmsRepository: FilmsRepository): GetCurrentApiDataSourceUseCase {
        return GetCurrentApiDataSourceUseCase(filmsRepository)
    }

    // usecase для задания состояния (filmsRepository)
    @Provides
    @Singleton
    fun provideChangeNetworkAvailabilityUseCase(filmsRepository: FilmsRepository): ChangeApiAvailabilityUseCase {
        return ChangeApiAvailabilityUseCase(filmsRepository)
    }

    // usecase для работы с кешем (filmsRepository)
    @Provides
    @Singleton
    fun provideDeleteAllCachedFilmsUseCase(filmsRepository: FilmsRepository): DeleteAllCachedFilmsUseCase {
        return DeleteAllCachedFilmsUseCase(filmsRepository)
    }

    @Provides
    @Singleton
    fun provideSetCacheModeUseCase(filmsRepository: FilmsRepository): SetCacheModeUseCase {
        return SetCacheModeUseCase(filmsRepository)
    }

    // usecase для работы с репозиторием настроек
    @Provides
    @Singleton
    fun provideGetAllSettingValue(
        settingsRepository: SettingsRepository,
        filmsRepository: FilmsRepository
    ): InitApplicationSettingsUseCase {
        return InitApplicationSettingsUseCase(settingsRepository, filmsRepository)
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