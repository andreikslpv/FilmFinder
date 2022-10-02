package com.andreikslpv.filmfinder

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import com.andreikslpv.filmfinder.model.Film

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        //Получаем наш фильм из переданного бандла
        val film = intent.extras?.get("film") as Film
        initView(film)
    }

    private fun initView(film: Film) {
        //Устанавливаем заголовок
        val detailsToolbar = findViewById<Toolbar>(R.id.details_toolbar)
        detailsToolbar.title = film.title
        //Устанавливаем картинку
        val detailsPoster = findViewById<AppCompatImageView>(R.id.details_poster)
        detailsPoster.setImageResource(film.poster)
        //Устанавливаем описание
        val detailsDescription = findViewById<TextView>(R.id.details_description)
        detailsDescription.text = film.descriptionFull
    }
}