package com.andreikslpv.filmfinder.data.datasource.local

import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel

interface FilmsLocalDataSource {

    fun getItems(): List<FilmLocalModel>

    fun saveItem(item: FilmLocalModel)
}