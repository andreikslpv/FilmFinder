package com.andreikslpv.filmfinder.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel

class DetailsFragment : Fragment() {
    private lateinit var film: FilmsLocalModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Получаем наш фильм из переданного бандла
        film = arguments?.get("film") as FilmsLocalModel

        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(film)
    }

    private fun initView(film: FilmsLocalModel) {
        //Устанавливаем заголовок
        val detailsToolbar = requireView().findViewById<Toolbar>(R.id.details_toolbar)
        detailsToolbar.title = film.title
        //Устанавливаем картинку
        val detailsPoster = requireView().findViewById<AppCompatImageView>(R.id.details_poster)
        detailsPoster.setImageResource(film.poster)
        //Устанавливаем описание
        val detailsDescription = requireView().findViewById<TextView>(R.id.details_description)
        detailsDescription.text = film.descriptionFull
    }
}