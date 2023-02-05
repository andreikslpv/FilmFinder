package com.andreikslpv.filmfinder.presentation.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.*
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.local.ChangeFilmLocalStateUseCase
import com.andreikslpv.filmfinder.domain.usecase.local.GetFilmLocalStateUseCase
import java.net.URL
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DetailsFragmentViewModel : ViewModel() {
    lateinit var filmLocalStateLiveData: LiveData<FilmDomainModel>

    @Inject
    lateinit var changeFilmLocalStateUseCase: ChangeFilmLocalStateUseCase

    @Inject
    lateinit var getFilmLocalStateUseCase: GetFilmLocalStateUseCase

    init {
        App.instance.dagger.inject(this)
    }

    fun setFilm(film: FilmDomainModel) {
        // получаем статус фильма в локальной бд
        filmLocalStateLiveData = getFilmLocalStateUseCase.execute(film.id)
        // проверяем находится фильм в хотя бы в одном списке, если нет то сохраняем его в бд
        val isFavorite = filmLocalStateLiveData.value?.isFavorite ?: false
        val isWatchLater = filmLocalStateLiveData.value?.isWatchLater ?: false
        if (!isFavorite && !isWatchLater) {
            Executors.newSingleThreadExecutor().execute {
                changeFilmLocalStateUseCase.execute(film)
            }
        }
    }

    fun changeWatchLaterField() {
        val film: FilmDomainModel = filmLocalStateLiveData.value!!
        film.isWatchLater = !film.isWatchLater
        Executors.newSingleThreadExecutor().execute {
            changeFilmLocalStateUseCase.execute(film)
        }
    }

    fun changeFavoritesField() {
        val film: FilmDomainModel = filmLocalStateLiveData.value!!
        film.isFavorite = !film.isFavorite
        Executors.newSingleThreadExecutor().execute {
            changeFilmLocalStateUseCase.execute(film)
        }
    }

    suspend fun loadWallpaper(url: String): Bitmap {
        return suspendCoroutine {
            val url = URL(url)
            val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            it.resume(bitmap)
        }
    }

}