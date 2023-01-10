package com.andreikslpv.filmfinder.data.datasource.local

import android.content.Context
import com.andreikslpv.filmfinder.data.datasource.local.models.FilmLocalModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

const val NAME_OF_LOCAL_STORAGE = "local.json"

@Singleton
class JsonDataSource @Inject constructor(context: Context) : FilmsLocalDataSource{

    private val gson: Gson = Gson()
    private val file = File("${context.filesDir}/$NAME_OF_LOCAL_STORAGE")

    override fun getItems(): List<FilmLocalModel> {
        val json = if (file.isFile) file.readText(Charsets.UTF_8) else ""
        return gson.fromJson(json, object : TypeToken<ArrayList<FilmLocalModel?>?>() {}.type)
            ?: emptyList()
    }

    override fun saveItem(item: FilmLocalModel) {
        val listToSave = if (file.isFile) {
            // получаем в изменяемый список все сохраненные фильмы
            val mutableItems: MutableList<FilmLocalModel> = getItems().toMutableList()
            val existItem = mutableItems.any { it.id == item.id }
            // если переданный фильм есть в списке
            // то меняем значения его полей isFavorite и isWatchLater на переданные
            // иначе добавляем переданный фильм в список
            if (existItem) {
                mutableItems.filter { it.id == item.id }[0].apply {
                    isFavorite = item.isFavorite
                    isWatchLater = item.isWatchLater
                }
            } else
                mutableItems.add(item)
            // удаляем из списка все фильмы, где поля isFavorite и isWatchLater равны false
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

}