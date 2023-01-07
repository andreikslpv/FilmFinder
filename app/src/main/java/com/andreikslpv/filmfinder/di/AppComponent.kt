package com.andreikslpv.filmfinder.di

 import android.content.Context
 import com.andreikslpv.filmfinder.di.modules.DataModule
 import com.andreikslpv.filmfinder.di.modules.DomainModule
 import com.andreikslpv.filmfinder.di.modules.RemoteModule
 import com.andreikslpv.filmfinder.presentation.ui.fragments.FavoritesFragment
 import com.andreikslpv.filmfinder.presentation.ui.fragments.HomeFragment
 import com.andreikslpv.filmfinder.presentation.ui.fragments.SelectionsFragment
 import com.andreikslpv.filmfinder.presentation.ui.fragments.WatchLaterFragment
 import com.andreikslpv.filmfinder.presentation.vm.*
 import dagger.BindsInstance
 import dagger.Component
 import javax.inject.Singleton

@Singleton
@Component(modules = [DomainModule::class, DataModule::class, RemoteModule::class])
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
    fun inject(homeFragment: HomeFragment)
    fun inject(favoritesFragment: FavoritesFragment)
    fun inject(watchLaterFragment: WatchLaterFragment)
    fun inject(selectionsFragment: SelectionsFragment)
}