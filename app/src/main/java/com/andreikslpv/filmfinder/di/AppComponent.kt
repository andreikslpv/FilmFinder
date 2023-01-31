package com.andreikslpv.filmfinder.di

 import android.content.Context
 import com.andreikslpv.filmfinder.data.repository.FilmsRepositoryImpl
 import com.andreikslpv.filmfinder.di.modules.*
 import com.andreikslpv.filmfinder.presentation.ui.MainActivity
 import com.andreikslpv.filmfinder.presentation.ui.fragments.*
 import com.andreikslpv.filmfinder.presentation.vm.*
 import dagger.BindsInstance
 import dagger.Component
 import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DomainModule::class, DataModule::class, RemoteModule::class, DatabaseModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

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