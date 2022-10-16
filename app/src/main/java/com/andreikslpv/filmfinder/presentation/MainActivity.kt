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
import com.andreikslpv.filmfinder.presentation.fragments.HomeFragment
import com.andreikslpv.filmfinder.repository.FilmsRepositoryImpl
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

const val TIME_INTERVAL = 2000
const val NUMBER_OF_HOME_FRAGMENT = 1

class MainActivity : AppCompatActivity() {
    private var backPressed = 0L

    val filmsRepository = FilmsRepositoryImpl(
        FilmsCacheDataSource(),
        FilmsApiDataSource(),
        FilmsLocalDataSource(File("local.json"), Gson())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMenus()

        //Запускаем фрагмент Home при старте
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, HomeFragment(), "home")
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



    fun launchDetailsFragment(film: FilmsLocalModel) {
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
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, it.title!!, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorites, R.id.watch_later, R.id.selections -> {
                    makeSnackbar(it.title!!)
                    true
                }
                else -> false
            }
        }
    }
}