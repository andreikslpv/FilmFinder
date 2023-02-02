package com.andreikslpv.filmfinder.domain.usecase.local

import androidx.lifecycle.LiveData
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class GetFilmLocalStateUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(filmId: String): LiveData<FilmDomainModel> {
        return filmsRepository.getFilmLocalState(filmId)
    }
}