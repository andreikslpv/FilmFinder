package com.andreikslpv.filmfinder.data.datasource

import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


class FilmsLocalDataSource(private val file: File, private val gson: Gson) {

    fun getItems(): List<FilmsLocalModel> {
        val json = if (file.isFile) file.readText(Charsets.UTF_8) else ""
        return gson.fromJson(json, object : TypeToken<ArrayList<FilmsLocalModel?>?>() {}.type)
            ?: emptyList()
    }

    fun saveItems(listToSave: List<FilmsLocalModel>) {
        file.writeText(gson.toJson(listToSave))
    }
}