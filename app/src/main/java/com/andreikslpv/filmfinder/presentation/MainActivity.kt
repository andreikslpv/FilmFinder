package com.andreikslpv.filmfinder.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.datasource.FilmsApiDataSource
import com.andreikslpv.filmfinder.datasource.FilmsCacheDataSource
import com.andreikslpv.filmfinder.datasource.FilmsLocalDataSource
import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel
import com.andreikslpv.filmfinder.domain.Pages
import com.andreikslpv.filmfinder.presentation.fragments.DetailsFragment
import com.andreikslpv.filmfinder.presentation.fragments.HomeFragment
import com.andreikslpv.filmfinder.repository.FilmsRepositoryImpl
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import java.io.File

const val TIME_INTERVAL = 2000
const val NUMBER_OF_HOME_FRAGMENT = 1

class MainActivity : AppCompatActivity() {
    private var backPressed = 0L
    private lateinit var bottomNavigation: BottomNavigationView
    lateinit var filmsRepository: FilmsRepositoryImpl
    var currentPage: Pages = Pages.HOME
        set(value) {
            if (field == value) return
            field = value
            launchHomeFragment()
        }

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
        when (currentPage) {
            Pages.HOME -> {
                bottomNavigation.menu.findItem(R.id.selections)
                    .setIcon(R.drawable.ic_baseline_video_library)
                bottomNavigation.menu.findItem(R.id.favorites)
                    .setIcon(R.drawable.ic_baseline_favorite_border)
                bottomNavigation.menu.findItem(R.id.watch_later)
                    .setIcon(R.drawable.ic_baseline_watch_later_border)

            }
            Pages.FAVORITES -> {
                bottomNavigation.menu.findItem(R.id.selections)
                    .setIcon(R.drawable.ic_baseline_video_library_border)
                bottomNavigation.menu.findItem(R.id.favorites)
                    .setIcon(R.drawable.ic_baseline_favorite)
                bottomNavigation.menu.findItem(R.id.watch_later)
                    .setIcon(R.drawable.ic_baseline_watch_later_border)

            }
            Pages.WATCH_LATER -> {
                bottomNavigation.menu.findItem(R.id.selections)
                    .setIcon(R.drawable.ic_baseline_video_library_border)
                bottomNavigation.menu.findItem(R.id.favorites)
                    .setIcon(R.drawable.ic_baseline_favorite_border)
                bottomNavigation.menu.findItem(R.id.watch_later)
                    .setIcon(R.drawable.ic_baseline_watch_later)
            }
        }
        // проверяем, если активен HomeFragment, то обновляем список фильмов
        // иначе запускаем HomeFragment
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_placeholder)
        if (currentFragment is HomeFragment) {
            currentFragment.refreshFilmsList()
        } else
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_placeholder, HomeFragment(), "home")
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

    private fun initMenus() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.selections -> {
                    currentPage = Pages.HOME
                    true
                }
                R.id.favorites -> {
                    currentPage = Pages.FAVORITES
                    true
                }
                R.id.watch_later -> {
                    currentPage = Pages.WATCH_LATER
                    true
                }
                else -> false
            }
        }
    }
}