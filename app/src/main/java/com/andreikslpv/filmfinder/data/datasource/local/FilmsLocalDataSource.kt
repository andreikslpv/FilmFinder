package com.andreikslpv.filmfinder.data.datasource.local

import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel

interface FilmsLocalDataSource {

    fun getItems(): List<FilmsLocalModel>

    fun saveItem(item: FilmsLocalModel)
}