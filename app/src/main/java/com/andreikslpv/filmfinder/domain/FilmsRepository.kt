package com.andreikslpv.filmfinder.domain

import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel

interface FilmsRepository {

    fun getAllFilms(): List<FilmsLocalModel>

    fun getAllLocalSavedFilms(): List<FilmsLocalModel>

    fun saveFilms(listFilmsToSave: List<FilmsLocalModel>)

}