package com.andreikslpv.filmfinder.domain

import androidx.paging.PagingData
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import kotlinx.coroutines.flow.Flow

interface FilmsRepository {

    fun getFilmsFromApi(page: Int, language: String, callback: ApiCallback)

    fun getSearchResultFromApi(query: String, page: Int, language: String, callback: ApiCallback)

    fun getPagedFilmsByCategory(language: String): Flow<PagingData<FilmDomainModel>>

    fun getAllLocalSavedFilms(): List<FilmDomainModel>

    fun saveFilmToLocal(film: FilmDomainModel)

}