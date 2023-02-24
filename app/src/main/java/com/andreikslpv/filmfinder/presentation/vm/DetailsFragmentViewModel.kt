package com.andreikslpv.filmfinder.presentation.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.local.ChangeFilmLocalStateUseCase
import com.andreikslpv.filmfinder.domain.usecase.local.GetFilmLocalStateUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.net.URL
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DetailsFragmentViewModel : ViewModel() {
    lateinit var filmLocalState: StateFlow<FilmDomainModel>
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var changeFilmLocalStateUseCase: ChangeFilmLocalStateUseCase

    @Inject
    lateinit var getFilmLocalStateUseCase: GetFilmLocalStateUseCase

    init {
        App.instance.dagger.inject(this)
    }

    fun setFilm(newFilm: FilmDomainModel) {
        // получаем статус фильма в локальной бд
        filmLocalState = getFilmLocalStateUseCase.execute(newFilm.id)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                FilmDomainModel()
            )
    }

    fun changeFilmLocalState(newFilm: FilmDomainModel) {
        scope.launch {
            changeFilmLocalStateUseCase.execute(newFilm)
        }
    }

    fun changeWatchLaterField() {
        val newFilm: FilmDomainModel = filmLocalState.value
        newFilm.isWatchLater = !newFilm.isWatchLater
        changeFilmLocalState(newFilm)
    }

    fun changeFavoritesField() {
        val newFilm: FilmDomainModel = filmLocalState.value
        newFilm.isFavorite = !newFilm.isFavorite
        changeFilmLocalState(newFilm)
    }

    suspend fun loadWallpaper(url: String): Bitmap {
        return suspendCoroutine {
            Executors.newSingleThreadExecutor().execute {
                val bitmap = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
                it.resume(bitmap)
            }
        }
    }

}