package com.andreikslpv.filmfinder.presentation

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.datasource.FilmsApiDataSource
import com.andreikslpv.filmfinder.datasource.FilmsCacheDataSource
import com.andreikslpv.filmfinder.datasource.FilmsLocalDataSource
import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel
import com.andreikslpv.filmfinder.presentation.fragments.DetailsFragment
import com.andreikslpv.filmfinder.presentation.fragments.HomeFragment
import com.andreikslpv.filmfinder.repository.FilmsRepositoryImpl
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import java.io.File

const val TIME_INTERVAL = 2000
const val TRANSITION_NAME = "image_name"
const val TRANSITION_DURATION = 800L

class MainActivity : AppCompatActivity() {
    private var backPressed = 0L
    private lateinit var bottomNavigation: BottomNavigationView
    lateinit var filmsRepository: FilmsRepositoryImpl
    var currentPage: Pages = Pages.DETAILS
        set(value) {
            if (field == value) return
            field = value
            setBottomNavigationIcon(field)
            if (field == Pages.HOME || field == Pages.FAVORITES || field == Pages.WATCH_LATER)
                launchHomeFragment()
        }

    private val detailsFragment = DetailsFragment()

    init {
        // задание анимации для shared element - imageview с постером
        val detailsTransition = TransitionSet()
        detailsTransition.apply {
            addTransition(ChangeBounds())
            addTransition(ChangeTransform())
            addTransition(ChangeImageTransform())
            ordering = TransitionSet.ORDERING_TOGETHER
        }
        detailsFragment.sharedElementEnterTransition =
            detailsTransition.setDuration(TRANSITION_DURATION)
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
        currentPage = Pages.HOME
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

    private fun launchHomeFragment() {
        // проверяем, если активен HomeFragment, то обновляем список фильмов
        // иначе запускаем HomeFragment
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_placeholder)
        if (currentFragment is HomeFragment)
            currentFragment.refreshFilmsList()
        else {
            // очищаем стек фрагментов перед запуском фрагмента Home
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_placeholder, HomeFragment(), "home")
                .commit()
        }
    }

    fun launchDetailsFragment(film: FilmsLocalModel, image: ImageView) {
        setBottomNavigationIcon(Pages.DETAILS)
        //Создаем "посылку"
        val bundle = Bundle()
        //Кладем переданный фильм в "посылку"
        bundle.putParcelable("film", film)
        //Прикрепляем "посылку" к фрагменту
        detailsFragment.arguments = bundle

        //Запускаем фрагмент Details
        supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(image, TRANSITION_NAME)
            .replace(R.id.fragment_placeholder, detailsFragment)
            .addToBackStack(null)
            .commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // double tap for exit
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_placeholder)
        if (currentFragment is HomeFragment) {
            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                finish()
            } else {
                Toast.makeText(this, R.string.main_backpress_message, Toast.LENGTH_SHORT).show()
            }
            backPressed = System.currentTimeMillis()
        } else {
            super.onBackPressed()
            setBottomNavigationIcon(currentPage)
        }
    }

    private fun setBottomNavigationIcon(page: Pages) {
        val selections = bottomNavigation.menu.findItem(R.id.selections)
        val favorites = bottomNavigation.menu.findItem(R.id.favorites)
        val watchLater = bottomNavigation.menu.findItem(R.id.watch_later)
        when (page) {
            Pages.HOME -> {
                selections.setIcon(R.drawable.ic_baseline_video_library)
                favorites.setIcon(R.drawable.ic_baseline_favorite_border)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later_border)
            }
            Pages.FAVORITES -> {
                selections.setIcon(R.drawable.ic_baseline_video_library_border)
                favorites.setIcon(R.drawable.ic_baseline_favorite)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later_border)
            }
            Pages.WATCH_LATER -> {
                selections.setIcon(R.drawable.ic_baseline_video_library_border)
                favorites.setIcon(R.drawable.ic_baseline_favorite_border)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later)
            }
            Pages.DETAILS -> {
                selections.setIcon(R.drawable.ic_baseline_video_library_border)
                favorites.setIcon(R.drawable.ic_baseline_favorite_border)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later_border)
            }
        }
    }
}