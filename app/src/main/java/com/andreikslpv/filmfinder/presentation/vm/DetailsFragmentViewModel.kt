package com.andreikslpv.filmfinder.presentation.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.local.ChangeFilmLocalStateUseCase
import com.andreikslpv.filmfinder.domain.usecase.local.GetFilmLocalStateUseCase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.URL
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DetailsFragmentViewModel : ViewModel() {
    lateinit var filmLocalState: Observable<FilmDomainModel>
    private var prevFilm = FilmDomainModel()

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
    }

    fun isNewFilm(newFilm: FilmDomainModel): Boolean {
        return if (newFilm.id != prevFilm.id) {
            prevFilm = newFilm
            true
        } else {
            false
        }
    }

    fun clearPrevFilm() {
        prevFilm = FilmDomainModel()
    }


    fun changeFilmLocalState(newFilm: FilmDomainModel) {
        Completable.fromSingle<FilmDomainModel> {
            changeFilmLocalStateUseCase.execute(newFilm)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun changeWatchLaterField() {
        val newFilm: FilmDomainModel = prevFilm
        newFilm.isWatchLater = !prevFilm.isWatchLater
        changeFilmLocalState(newFilm)
    }

    fun changeFavoritesField() {
        val newFilm: FilmDomainModel = prevFilm
        newFilm.isFavorite = !prevFilm.isFavorite
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