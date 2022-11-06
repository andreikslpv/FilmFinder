package com.andreikslpv.filmfinder.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.datasource.FilmsApiDataSource
import com.andreikslpv.filmfinder.datasource.FilmsCacheDataSource
import com.andreikslpv.filmfinder.datasource.FilmsLocalDataSource
import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel
import com.andreikslpv.filmfinder.presentation.fragments.*
import com.andreikslpv.filmfinder.repository.FilmsRepositoryImpl
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import java.io.File


const val TIME_INTERVAL = 2000
const val TRANSITION_NAME_FOR_IMAGE = "image_name"
const val TRANSITION_NAME_FOR_TEXT = "text_name"
const val TRANSITION_DURATION = 800L

class MainActivity : AppCompatActivity() {
    private var backPressed = 0L
    private lateinit var bottomNavigation: BottomNavigationView
    lateinit var filmsRepository: FilmsRepositoryImpl
    private var currentFragmentsType: FragmentsType = FragmentsType.DETAILS
        set(value) {
            if (field == value) return
            field = value
            setBottomNavigationIcon(field)
        }

    private val detailsFragment = DetailsFragment()

    init {
        // задание анимации для shared elements - imageview с постером и textview с описанием
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

        //------- эмуляция Splash Screen с векторной анимацией
        val image = findViewById<ImageView>(R.id.image_logo)
        //Создаем экземпляр AnimatedVectorDrawableCompat, чтобы была совместимость с различными версиями Android
        //в параметры передаем контекст и файл с анимацей вектора
        val animatedVectorDrawable =
            AnimatedVectorDrawableCompat.create(this, R.drawable.anim_logo_build)
        //Устанавливаем animatedVectorDrawable в наше view
        image.setImageDrawable(animatedVectorDrawable)
        animatedVectorDrawable?.registerAnimationCallback(object :
            Animatable2Compat.AnimationCallback() {
            // Когда заканчивается анимация запускаем нормальную работу приложения
            override fun onAnimationEnd(drawable: Drawable) {
                Thread.sleep(500)
                // прячем imageview с анимацией
                image.visibility = INVISIBLE

                initBottomNavigationMenu()

                val directory = application.filesDir
                filmsRepository = FilmsRepositoryImpl(
                    FilmsCacheDataSource(),
                    FilmsApiDataSource(),
                    FilmsLocalDataSource(File("$directory/local.json"), Gson())
                )

                // запускаем фрагмент Home
                changeFragment(HomeFragment(), FragmentsType.HOME)
            }
        })
        //запускаем анимацию
        animatedVectorDrawable?.start()
    }

    private fun initBottomNavigationMenu() {
        bottomNavigation = findViewById(R.id.bottom_navigation)

        // показываем BottomNavigation (после Splash Screen)
        bottomNavigation.visibility = VISIBLE

        bottomNavigation.setOnItemSelectedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_placeholder)
            when (it.itemId) {
                R.id.home -> {
                    // если активен не HomeFragment, то запускаем HomeFragment
                    if (currentFragment !is HomeFragment) {
                        val fragment = checkFragmentExistence(FragmentsType.HOME)
                        //В первом параметре, если фрагмент не найден и метод вернул null, то с помощью
                        //элвиса мы вызываем создание нового фрагмента
                        changeFragment(fragment ?: HomeFragment(), FragmentsType.HOME)
                    }
                    true
                }
                R.id.favorites -> {
                    if (currentFragment !is FavoritesFragment) {
                        val fragment = checkFragmentExistence(FragmentsType.FAVORITES)
                        changeFragment(fragment ?: FavoritesFragment(), FragmentsType.FAVORITES)
                    }
                    true
                }
                R.id.watch_later -> {
                    if (currentFragment !is WatchLaterFragment) {
                        val fragment = checkFragmentExistence(FragmentsType.WATCH_LATER)
                        changeFragment(fragment ?: WatchLaterFragment(), FragmentsType.WATCH_LATER)
                    }
                    true
                }
                R.id.selections -> {
                    if (currentFragment !is SelectionsFragment) {
                        val fragment = checkFragmentExistence(FragmentsType.SELECTIONS)
                        changeFragment(fragment ?: SelectionsFragment(), FragmentsType.SELECTIONS)
                    }
                    true
                }
                else -> false
            }
        }
    }

    //Ищем фрагмент по тегу, если он есть то возвращаем его, если нет, то null
    private fun checkFragmentExistence(type: FragmentsType): Fragment? =
        supportFragmentManager.findFragmentByTag(type.tag)

    private fun changeFragment(fragment: Fragment, type: FragmentsType) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment, type.tag)
            .addToBackStack(null)
            .commit()
        currentFragmentsType = type
    }

    fun launchDetailsFragment(film: FilmsLocalModel, image: ImageView, text: TextView) {
        //Создаем "посылку"
        val bundle = Bundle()
        //Кладем переданный фильм в "посылку"
        bundle.putParcelable("film", film)
        //Кладем тип фрагмента из которого происходит вызов в "посылку"
        bundle.putParcelable("type", currentFragmentsType)
        //Прикрепляем "посылку" к фрагменту
        detailsFragment.arguments = bundle

        //Запускаем фрагмент Details
        supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(image, TRANSITION_NAME_FOR_IMAGE)
            .addSharedElement(text, TRANSITION_NAME_FOR_TEXT)
            .replace(R.id.fragment_placeholder, detailsFragment, "details")
            .addToBackStack(null)
            .commit()
        setBottomNavigationIcon(FragmentsType.DETAILS)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // double tap for exit
        when (supportFragmentManager.findFragmentById(R.id.fragment_placeholder)) {
            is HomeFragment, is FavoritesFragment, is WatchLaterFragment, is SelectionsFragment -> {
                if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(this, R.string.main_backpress_message, Toast.LENGTH_SHORT).show()
                }
                backPressed = System.currentTimeMillis()
            }
            else -> {
                super.onBackPressed()
                setBottomNavigationIcon(currentFragmentsType)
            }
        }
    }

    private fun setBottomNavigationIcon(type: FragmentsType) {
        val home = bottomNavigation.menu.findItem(R.id.home)
        val favorites = bottomNavigation.menu.findItem(R.id.favorites)
        val watchLater = bottomNavigation.menu.findItem(R.id.watch_later)
        val selections = bottomNavigation.menu.findItem(R.id.selections)
        when (type) {
            FragmentsType.HOME -> {
                home.setIcon(R.drawable.ic_baseline_home)
                favorites.setIcon(R.drawable.ic_baseline_favorite_border)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later_border)
                selections.setIcon(R.drawable.ic_baseline_selections_border)
            }
            FragmentsType.FAVORITES -> {
                home.setIcon(R.drawable.ic_baseline_home_border)
                favorites.setIcon(R.drawable.ic_baseline_favorite)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later_border)
                selections.setIcon(R.drawable.ic_baseline_selections_border)
            }
            FragmentsType.WATCH_LATER -> {
                home.setIcon(R.drawable.ic_baseline_home_border)
                favorites.setIcon(R.drawable.ic_baseline_favorite_border)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later)
                selections.setIcon(R.drawable.ic_baseline_selections_border)
            }
            FragmentsType.SELECTIONS -> {
                home.setIcon(R.drawable.ic_baseline_home_border)
                favorites.setIcon(R.drawable.ic_baseline_favorite_border)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later_border)
                selections.setIcon(R.drawable.ic_baseline_selections)
            }
            FragmentsType.DETAILS -> {
                home.setIcon(R.drawable.ic_baseline_home_border)
                favorites.setIcon(R.drawable.ic_baseline_favorite_border)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later_border)
                selections.setIcon(R.drawable.ic_baseline_selections_border)
            }
        }
    }

    fun setBackground(newBackground: Drawable?) {
        if (newBackground != null) {
            val mainActivityLayout = findViewById<ConstraintLayout>(R.id.main_layout)
            mainActivityLayout.background = newBackground
        }
    }
}