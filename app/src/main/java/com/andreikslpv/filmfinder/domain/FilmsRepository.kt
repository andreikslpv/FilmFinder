package com.andreikslpv.filmfinder.domain

import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

interface FilmsRepository {

    fun getAllFilmsByPage(): List<FilmDomainModel>

    fun getAllLocalSavedFilms(): List<FilmDomainModel>

    fun saveFilmToLocal(film: FilmDomainModel)

}