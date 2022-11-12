package com.andreikslpv.filmfinder.domain

import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel

interface FilmsRepository {

    fun getAllFilmsByPage(): List<FilmsLocalModel>

    fun getAllLocalSavedFilms(): List<FilmsLocalModel>

    fun saveFilmToLocal(film: FilmsLocalModel)

}