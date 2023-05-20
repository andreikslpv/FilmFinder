package com.andreikslpv.filmfinder.presentation.vm

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.apicache.GetFilmByIdFromApiUseCase
import com.andreikslpv.filmfinder.presentation.ui.utils.AppConstants.FILM_LINK
import com.andreikslpv.filmfinder.presentation.ui.utils.FragmentsType
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@SuppressLint("CheckResult")
class MainActivityViewModel : ViewModel() {
    val currentFragmentLiveData: MutableLiveData<FragmentsType> =
        MutableLiveData(FragmentsType.NONE)
    private var previousFragmentsType = FragmentsType.NONE

    private val message = MutableLiveData("")
    val messageLiveData: LiveData<String> = message

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    @Inject
    lateinit var getFilmByIdFromApiUseCase: GetFilmByIdFromApiUseCase

    var promo: Observable<List<FilmDomainModel>>

    init {
        App.instance.dagger.inject(this)

        promo = Observable.create<List<FilmDomainModel>> { emitter ->
            if (!App.instance.isPromoShown) {
                remoteConfig.fetch().addOnCompleteListener { task ->
                    //Если все получилось успешно
                    if (task.isSuccessful) {
                        //активируем последний полученный конфиг с сервера
                        remoteConfig.activate()
                        //Получаем ссылку
                        val filmLink = remoteConfig.getString(FILM_LINK)
                        //Если поле не пустое
                        if (filmLink.isNotBlank()) {
                            //Ставим флаг, что уже промо показали
                            App.instance.isPromoShown = true
                            getFilmByIdFromApiUseCase.execute(filmLink)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        emitter.onNext(it)
                                    },
                                    {
                                        println("AAA tryToGetPromo error ${it.message}")
                                    }
                                )
                        }
                    }
                }
            }
        }
    }

    fun setCurrentFragment(newFragment: FragmentsType) {
        if (this.currentFragmentLiveData.value == newFragment) {
            return
        } else {
            previousFragmentsType = this.currentFragmentLiveData.value ?: FragmentsType.NONE
            this.currentFragmentLiveData.value = newFragment
        }
    }

    fun setCurrentFragmentFromPrevious() {
        this.currentFragmentLiveData.value = previousFragmentsType
    }

    fun getCurrentFragment() = currentFragmentLiveData.value

    fun setMessage(newMessage: String) {
        if (newMessage == message.value) return
        else message.value = newMessage
    }
}