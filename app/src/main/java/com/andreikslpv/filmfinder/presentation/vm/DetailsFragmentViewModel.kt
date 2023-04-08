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
import javax.inject.Inject

class DetailsFragmentViewModel : ViewModel() {
    lateinit var filmLocalState: Observable<FilmDomainModel>
    var prevFilm = FilmDomainModel()
        private set

    @Inject
    lateinit var changeFilmLocalStateUseCase: ChangeFilmLocalStateUseCase

    @Inject
    lateinit var getFilmLocalStateUseCase: GetFilmLocalStateUseCase

    init {
        App.instance.dagger.inject(this)
    }

    fun setFilm(newFilm: FilmDomainModel) {
        // пробуем добавить фильм в БД, если уже есть в БД, то не добавляем
        changeFilmLocalState(newFilm, false)
        // получаем статус фильма в локальной бд
        filmLocalState = getFilmLocalStateUseCase.execute(newFilm.id)
        prevFilm = newFilm
    }

    fun clearPrevFilm() {
        prevFilm = FilmDomainModel()
    }


    private fun changeFilmLocalState(newFilm: FilmDomainModel, replace: Boolean) {
        Completable.fromSingle<FilmDomainModel> {
            try {
                changeFilmLocalStateUseCase.execute(newFilm, replace)
            } catch (e: Exception) {
            }
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun changeWatchLaterField(reminderTime: Long) {
        val newFilm: FilmDomainModel = prevFilm
        newFilm.isWatchLater = !prevFilm.isWatchLater
        newFilm.reminderTime = reminderTime
        changeFilmLocalState(newFilm, true)
    }

    fun changeFavoritesField() {
        val newFilm: FilmDomainModel = prevFilm
        newFilm.isFavorite = !prevFilm.isFavorite
        changeFilmLocalState(newFilm, true)
    }

    fun loadWallpaper(url: String): Bitmap {
        return BitmapFactory.decodeStream(
            URL(url).openConnection().getInputStream()
        )
    }

}