package com.andreikslpv.filmfinder.domain

import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

interface FilmsRepository {

    fun getFilmsFromApi(page: Int, language: String, callback: ApiCallback)

    fun getSearchResultFromApi(query: String, page: Int, language: String, callback: ApiCallback)

    fun getAllLocalSavedFilms(): List<FilmDomainModel>

    fun saveFilmToLocal(film: FilmDomainModel)

}