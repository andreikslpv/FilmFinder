package com.andreikslpv.filmfinder.domain.usecase.local

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import io.reactivex.rxjava3.core.Observable

class GetWatchLaterFilmsUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(): Observable<List<FilmDomainModel>> {
        return filmsRepository.getWatchLaterFilms().map { list ->
            list.forEach { item ->
                if (item.reminderTime < System.currentTimeMillis()) {
                    item.isWatchLater = false
                    item.reminderTime = 0L
                    filmsRepository.saveFilmToLocal(item, true)
                }
            }
            list.filter {
                it.isWatchLater
            }
            val newList = list.sortedBy {
                it.reminderTime
            }
            newList
        }
    }
}