package com.andreikslpv.filmfinder.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.datasource.FilmsApiDataSource
import com.andreikslpv.filmfinder.datasource.FilmsCacheDataSource
import com.andreikslpv.filmfinder.datasource.FilmsLocalDataSource
import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel
import com.andreikslpv.filmfinder.presentation.fragments.DetailsFragment
import com.andreikslpv.filmfinder.presentation.fragments.FavoritesFragment
import com.andreikslpv.filmfinder.presentation.fragments.HomeFragment
import com.andreikslpv.filmfinder.presentation.fragments.WatchLaterFragment
import com.andreikslpv.filmfinder.repository.FilmsRepositoryImpl
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.io.File

const val TIME_INTERVAL = 2000
const val NUMBER_OF_HOME_FRAGMENT = 1
const val FAVORITES = 1
const val WATCH_LATER = 2

class MainActivity : AppCompatActivity() {
    private var backPressed = 0L
    private lateinit var bottomNavigation: BottomNavigationView
    lateinit var filmsRepository: FilmsRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMenus()
        val directory = application.filesDir
        filmsRepository = FilmsRepositoryImpl(
            FilmsCacheDataSource(),
            FilmsApiDataSource(),
            FilmsLocalDataSource(File("$directory/local.json"), Gson())
        )

        // запускаем фрагмент Home
        launchHomeFragment()
    }

    private fun launchHomeFragment() {
        bottomNavigation.menu.findItem(R.id.selections)
            .setIcon(R.drawable.ic_baseline_video_library)
        bottomNavigation.menu.findItem(R.id.favorites)
            .setIcon(R.drawable.ic_baseline_favorite_border)
        bottomNavigation.menu.findItem(R.id.watch_later)
            .setIcon(R.drawable.ic_baseline_watch_later_border)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, HomeFragment(), "home")
            .addToBackStack(null)
            .commit()
    }

    private fun launchFavoritesFragment() {
        bottomNavigation.menu.findItem(R.id.selections)
            .setIcon(R.drawable.ic_baseline_video_library_border)
        bottomNavigation.menu.findItem(R.id.favorites).setIcon(R.drawable.ic_baseline_favorite)
        bottomNavigation.menu.findItem(R.id.watch_later)
            .setIcon(R.drawable.ic_baseline_watch_later_border)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, FavoritesFragment(), "favorites")
            .addToBackStack(null)
            .commit()
    }

    private fun launchWatchLaterFragment() {
        bottomNavigation.menu.findItem(R.id.selections)
            .setIcon(R.drawable.ic_baseline_video_library_border)
        bottomNavigation.menu.findItem(R.id.favorites).setIcon(R.drawable.ic_baseline_favorite_border)
        bottomNavigation.menu.findItem(R.id.watch_later)
            .setIcon(R.drawable.ic_baseline_watch_later)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, WatchLaterFragment(), "watch_later")
            .addToBackStack(null)
            .commit()
    }

    fun launchDetailsFragment(film: FilmsLocalModel) {
        bottomNavigation.menu.findItem(R.id.selections)
            .setIcon(R.drawable.ic_baseline_video_library_border)
        bottomNavigation.menu.findItem(R.id.favorites)
            .setIcon(R.drawable.ic_baseline_favorite_border)
        bottomNavigation.menu.findItem(R.id.watch_later)
            .setIcon(R.drawable.ic_baseline_watch_later_border)
        //Создаем "посылку"
        val bundle = Bundle()
        //Кладем наш фильм в "посылку"
        bundle.putParcelable("film", film)
        //Кладем фрагмент с деталями в перменную
        val fragment = DetailsFragment()
        //Прикрепляем нашу "посылку" к фрагменту
        fragment.arguments = bundle

        //Запускаем фрагмент Details
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(null)
            .commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // double tap for exit
        if (supportFragmentManager.backStackEntryCount == NUMBER_OF_HOME_FRAGMENT) {
            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed()
                finish()
            } else {
                Toast.makeText(this, R.string.main_backpress_message, Toast.LENGTH_SHORT).show()
            }
            backPressed = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

    private fun makeSnackbar(text: CharSequence) {
        val snackbar = Snackbar.make(findViewById(R.id.main_layout), text, Snackbar.LENGTH_SHORT)
        if (supportFragmentManager.backStackEntryCount == NUMBER_OF_HOME_FRAGMENT) {
            snackbar.setAction(R.string.main_change_ad) {
                val fragment: HomeFragment =
                    supportFragmentManager.findFragmentByTag("home") as HomeFragment
                fragment.changeAd()
            }
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.red))
        }
        snackbar.show()
    }

    private fun initMenus() {
        val topAppBar = findViewById<MaterialToolbar>(R.id.top_app_bar)
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    makeSnackbar(it.title!!)
                    true
                }
                else -> false
            }
        }
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.selections -> {
                    //Запускаем фрагмент Home
                    launchHomeFragment()
                    true
                }
                R.id.favorites -> {
                    //Запускаем фрагмент Favorites
                    launchFavoritesFragment()
                    true
                }
                R.id.watch_later -> {
                    //Запускаем фрагмент WatchLater
                    launchWatchLaterFragment()
                    true
                }
                else -> false
            }
        }
    }
}