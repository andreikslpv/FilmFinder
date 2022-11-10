package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel

class ChangeFilmLocalStateUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(film: FilmsLocalModel) {
        // получаем в изменяемый список все сохраненные фильмы
        val mutableFilms: MutableList<FilmsLocalModel> = filmsRepository.getAllLocalSavedFilms().toMutableList()
        val existFilm = mutableFilms.any { it.id == film.id }
        // если переданный фильм есть в списке
        // то меняем значения его полей isFavorite и isWatchLater на переданные
        // иначе добавляем переданный фильм в список
        if (existFilm) {
            mutableFilms.filter { it.id == film.id }[0].apply {
                isFavorite = film.isFavorite
                isWatchLater = film.isWatchLater
            }
        } else
            mutableFilms.add(film)
        // удаляем из списка все фильмы, где поля isFavorite и isWatchLater равны false
        mutableFilms.removeAll {
            !it.isFavorite && !it.isWatchLater
        }
        // сохраняем список в файл
        filmsRepository.saveFilms(mutableFilms)
    }
}