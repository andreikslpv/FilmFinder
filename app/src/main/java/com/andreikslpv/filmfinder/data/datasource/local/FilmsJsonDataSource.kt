package com.andreikslpv.filmfinder.data.datasource.local

import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


class FilmsJsonDataSource(private val file: File, private val gson: Gson) : FilmsLocalDataSource{

    override fun getItems(): List<FilmsLocalModel> {
        val json = if (file.isFile) file.readText(Charsets.UTF_8) else ""
        return gson.fromJson(json, object : TypeToken<ArrayList<FilmsLocalModel?>?>() {}.type)
            ?: emptyList()
    }

    override fun saveItem(item: FilmsLocalModel) {
        val listToSave = if (file.isFile) {
            val mutableItems: MutableList<FilmsLocalModel> = getItems().toMutableList()
            val existItem = mutableItems.any { it.id == item.id }
            if (existItem) {
                mutableItems.filter { it.id == item.id }[0].apply {
                    isFavorite = item.isFavorite
                    isWatchLater = item.isWatchLater
                }
            } else
                mutableItems.add(item)
            mutableItems.removeAll {
                !it.isFavorite && !it.isWatchLater
            }
            mutableItems
        } else {
            val firstItemWhenFileIsEmpty = listOf(item)
            firstItemWhenFileIsEmpty
        }
        file.writeText(gson.toJson(listToSave))
    }

    /*fun saveItems(listToSave: List<FilmsLocalModel>) {
        file.writeText(gson.toJson(listToSave))
    }*/
}