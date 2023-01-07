package com.andreikslpv.filmfinder.di

import com.andreikslpv.filmfinder.di.modules.AppModule
import com.andreikslpv.filmfinder.di.modules.DataModule
import com.andreikslpv.filmfinder.di.modules.DomainModule
import com.andreikslpv.filmfinder.presentation.ui.fragments.*
import com.andreikslpv.filmfinder.presentation.vm.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DomainModule::class, DataModule::class])
interface AppComponent {
    //методы для того, чтобы появилась возможность внедрять зависимости в требуемые классы
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
    fun inject(favoritesFragmentViewModel: FavoritesFragmentViewModel)
    fun inject(watchLaterFragmentViewModel: WatchLaterFragmentViewModel)
    fun inject(selectionsFragmentViewModel: SelectionsFragmentViewModel)
    fun inject(detailsFragmentViewModel: DetailsFragmentViewModel)
    fun inject(homeFragment: HomeFragment)
    fun inject(favoritesFragment: FavoritesFragment)
    fun inject(watchLaterFragment: WatchLaterFragment)
    fun inject(selectionsFragment: SelectionsFragment)
}