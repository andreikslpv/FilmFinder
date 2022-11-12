package com.andreikslpv.filmfinder.data.datasource.cache

import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class FilmsCacheDataSource {
    private val filmsMutableList: MutableList<FilmDomainModel> = mutableListOf()
    var films: List<FilmDomainModel>
        get() = filmsMutableList
        set(value) {
            filmsMutableList.clear()
            filmsMutableList.addAll(value)
        }
}