package com.andreikslpv.filmfinder.datasource

import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


class FilmsLocalDataSource(private val file: File, private val gson: Gson) {
    fun getItems(): List<FilmsLocalModel> {
        val json = if (file.isFile) file.readText(Charsets.UTF_8) else ""

        return gson.fromJson(json, object : TypeToken<ArrayList<FilmsLocalModel?>?>() {}.type)
            ?: emptyList()
    }

    fun changeItem(item: FilmsLocalModel): Boolean {
        var isCanRemove = false
        val listToSave = if (file.isFile) {
            val mutableItems: MutableList<FilmsLocalModel> = getItems().toMutableList()
            val existItem = mutableItems.any { it.id == item.id }
            if (existItem) {
                val itemToRemove = mutableItems.filter { it.id == item.id }[0].apply {
                    isFavorite = item.isFavorite
                    isWatchLater = item.isWatchLater
                    if (!isFavorite && !isWatchLater)
                        isCanRemove = true
                }
                if (isCanRemove)
                    mutableItems.remove(itemToRemove)
            }
            mutableItems
        } else {
            val firstItemWhenFileIsEmpty = listOf(item)
            firstItemWhenFileIsEmpty
        }
        if (!isCanRemove)
            file.writeText(gson.toJson(listToSave))

        return true
    }


    fun saveItem(objectToSave: FilmsLocalModel): Boolean {
        val listToSave = if (file.isFile) {
            val mutableItems: MutableList<FilmsLocalModel> = getItems().toMutableList()
            var change = false
            mutableItems.map {
                if (it.id == objectToSave.id) {
                    it.isFavorite = objectToSave.isFavorite
                    it.isWatchLater = objectToSave.isWatchLater
                    change = true
                }
            }
            if (!change) mutableItems.add(objectToSave)
            mutableItems
        } else {
            val firstItemWhenFileIsEmpty = listOf(objectToSave)
            firstItemWhenFileIsEmpty
        }
        file.writeText(gson.toJson(listToSave))

        return isObjectSaved(objectToSave)
    }

    private fun isObjectSaved(film: FilmsLocalModel): Boolean {
        return getItems().any { film.id == it.id }
    }

    fun removeItem(objectToRemove: FilmsLocalModel): Boolean {
        if (file.isFile) {
            val mutableItems: MutableList<FilmsLocalModel> = getItems().toMutableList()
            val existItem = mutableItems.any { it.id == objectToRemove.id }
            if (existItem) {
                var isCanRemove = false
                val itemToRemove = mutableItems.filter { it.id == objectToRemove.id }[0].apply {
                    isFavorite = objectToRemove.isFavorite
                    isWatchLater = objectToRemove.isWatchLater
                    if (!isFavorite && !isWatchLater)
                        isCanRemove = true
                }
                if (isCanRemove)
                    mutableItems.remove(itemToRemove)
                file.writeText(gson.toJson(mutableItems.toList()))
            }


            /*val existItem = mutableItems.any { it.id == itemId }
                if (existItem) {
                    val itemToRemove = mutableItems.filter { it.id == itemId }[0]
                    mutableItems.remove(itemToRemove)
                    file.writeText(gson.toJson(mutableItems.toList()))
                }*/
        }

        return isItemRemoved(objectToRemove.id)
    }


    private fun isItemRemoved(itemId: Int): Boolean {
        return getItems().any { it.id == itemId }.not()
    }
}