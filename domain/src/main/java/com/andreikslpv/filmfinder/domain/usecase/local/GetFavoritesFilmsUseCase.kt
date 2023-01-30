package com.andreikslpv.filmfinder.domain.usecase.local

import androidx.lifecycle.LiveData
import com.andreikslpv.filmfinder.domain.FilmsRepository
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

class GetFavoritesFilmsUseCase(private val filmsRepository: FilmsRepository) {
    fun execute(): LiveData<List<FilmDomainModel>> {
        return filmsRepository.getFavoritesFilms()
    }
}