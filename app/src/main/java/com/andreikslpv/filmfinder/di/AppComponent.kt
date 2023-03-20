package com.andreikslpv.filmfinder.di

import com.andreikslpv.filmfinder.data.di.DataProvider
import com.andreikslpv.filmfinder.data.repository.FilmsRepositoryImpl
import com.andreikslpv.filmfinder.di.modules.AppModule
import com.andreikslpv.filmfinder.di.modules.DomainModule
import com.andreikslpv.filmfinder.presentation.ui.MainActivity
import com.andreikslpv.filmfinder.presentation.ui.fragments.*
import com.andreikslpv.filmfinder.presentation.vm.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [DataProvider::class],
    modules = [AppModule::class, DomainModule::class]
)
interface AppComponent {

    //методы для того, чтобы появилась возможность внедрять зависимости в требуемые классы
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
    fun inject(favoritesFragmentViewModel: FavoritesFragmentViewModel)
    fun inject(watchLaterFragmentViewModel: WatchLaterFragmentViewModel)
    fun inject(selectionsFragmentViewModel: SelectionsFragmentViewModel)
    fun inject(detailsFragmentViewModel: DetailsFragmentViewModel)
    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
    fun inject(mainActivity: MainActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(favoritesFragment: FavoritesFragment)
    fun inject(watchLaterFragment: WatchLaterFragment)
    fun inject(selectionsFragment: SelectionsFragment)
    fun inject(detailsFragment: DetailsFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(filmsRepositoryImpl: FilmsRepositoryImpl)
}