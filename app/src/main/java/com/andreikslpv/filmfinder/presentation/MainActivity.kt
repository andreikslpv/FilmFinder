package com.andreikslpv.filmfinder.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.model.Film
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var posterCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMenus()

        //Запускаем фрагмент Home при старте
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, HomeFragment())
            .addToBackStack(null)
            .commit()
    }

    fun launchDetailsFragment(film: Film) {
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

    private fun changePosterAndToast(text: CharSequence) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        /*val recyclerView: RecyclerView = findViewById(R.id.ad_recycler)
        var i = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(2))
        i++
        if (i >= posterCount) i = 0
        recyclerView.scrollToPosition(i)*/
    }

    private fun initMenus() {
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    changePosterAndToast(it.title!!)
                    true
                }
                else -> false
            }
        }

        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorites, R.id.watch_later, R.id.selections -> {
                    changePosterAndToast(it.title!!)
                    true
                }
                else -> false
            }
        }
    }
}