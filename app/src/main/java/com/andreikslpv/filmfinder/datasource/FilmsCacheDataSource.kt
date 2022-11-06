package com.andreikslpv.filmfinder.datasource

import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel

class FilmsCacheDataSource {
    private val filmsMutableList: MutableList<FilmsLocalModel> = mutableListOf()
    var films: List<FilmsLocalModel>
        get() = filmsMutableList
        set(value) {
            filmsMutableList.clear()
            filmsMutableList.addAll(value)
        }
}