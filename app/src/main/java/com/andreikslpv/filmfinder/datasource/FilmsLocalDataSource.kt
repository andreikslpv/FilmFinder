package com.andreikslpv.filmfinder.datasource

import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


class FilmsLocalDataSource(private val file: File, private val gson: Gson) {
    fun getItems(): List<FilmsLocalModel> {
        val json = if (file.isFile) file.readText(Charsets.UTF_8) else ""

        return gson.fromJson(json, object : TypeToken<ArrayList<FilmsLocalModel?>?>() {}.type) ?: emptyList()
    }

    fun saveItem(objectToSave: FilmsLocalModel): Boolean {
        val listToSave = if (file.isFile) {
            val mutableItems: MutableList<FilmsLocalModel> = getItems().toMutableList()
            mutableItems.add(objectToSave)
            mutableItems
        } else {
            val firstItemWhenFileIsEmpty = listOf(objectToSave)
            firstItemWhenFileIsEmpty
        }

        file.writeText(gson.toJson(listToSave))

        return isObjectSaved(objectToSave)
    }

    private fun isObjectSaved(beer: FilmsLocalModel): Boolean {
        return getItems().any { beer.id == it.id }
    }

    fun removeItem(itemId: Int): Boolean {
        if (file.isFile) {
            val mutableItems: MutableList<FilmsLocalModel> = getItems().toMutableList()
            val existItem = mutableItems.any { it.id == itemId }
            if (existItem) {
                val itemToRemove = mutableItems.filter { it.id == itemId }[0]
                mutableItems.remove(itemToRemove)
                file.writeText(gson.toJson(mutableItems.toList()))
            }
        }

        return isItemRemoved(itemId)
    }


    private fun isItemRemoved(itemId: Int): Boolean {
        return getItems().any { it.id == itemId }.not()
    }
}