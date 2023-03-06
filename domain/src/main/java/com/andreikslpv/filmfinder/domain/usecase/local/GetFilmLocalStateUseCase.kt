package com.andreikslpv.filmfinder.domain.usecase.local

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import io.reactivex.rxjava3.core.Observable

class GetFilmLocalStateUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(filmId: String): Observable<FilmDomainModel> {
        return filmsRepository.getFilmLocalState(filmId)
    }
}