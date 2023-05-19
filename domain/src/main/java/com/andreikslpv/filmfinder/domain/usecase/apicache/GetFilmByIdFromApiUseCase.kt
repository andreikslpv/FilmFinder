package com.andreikslpv.filmfinder.domain.usecase.apicache

import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import io.reactivex.rxjava3.core.Single

class GetFilmByIdFromApiUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(filmId: String): Single<List<FilmDomainModel>> {
        return filmsRepository.getFilmByIdFromApi(filmId)
    }
}