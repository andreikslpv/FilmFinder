package com.andreikslpv.filmfinder.domain

import com.andreikslpv.filmfinder.domain.model.Film

interface FilmsRepository {
    fun getAllFilms(): List<Film>
    fun getRandomFilms(count: Int): List<Film>
    fun getFilmById(id: Int): Film?
}