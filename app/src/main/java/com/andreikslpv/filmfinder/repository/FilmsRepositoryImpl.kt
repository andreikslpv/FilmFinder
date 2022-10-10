package com.andreikslpv.filmfinder.repository

import com.andreikslpv.filmfinder.datasource.FilmsCacheDataSource
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.model.Film
import kotlin.random.Random

class FilmsRepositoryImpl(private val cacheDataSource: FilmsCacheDataSource) : FilmsRepository {
    override fun getAllFilms(): List<Film> {
        return cacheDataSource.filmsDataBase.ifEmpty { emptyList() }
    }

    override fun getRandomFilms(count: Int): List<Film> {
        val randomList = mutableListOf<Film>()
        lateinit var film: Film
        var i = 0
        var index: Int
        if (cacheDataSource.filmsDataBase.isNotEmpty() && cacheDataSource.filmsDataBase.size >= count) {
            while (i < count) {
                println(i)
                index = Random.nextInt(0, cacheDataSource.filmsDataBase.size - 1)
                film = cacheDataSource.filmsDataBase[index]
                if (!randomList.contains(film)) {
                    randomList.add(film)
                    i++
                }
            }
        } else
            return emptyList()
        return randomList
    }

    override fun getFilmById(id: Int): Film? {
        TODO("Not yet implemented")
    }
}