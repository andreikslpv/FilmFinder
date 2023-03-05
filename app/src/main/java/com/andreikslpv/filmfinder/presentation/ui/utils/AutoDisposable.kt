package com.andreikslpv.filmfinder.presentation.ui.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

//Наследуем от LifecycleObserver
class AutoDisposable : LifecycleObserver {
    //Используем CompositeDisposable для отмены всех Observable
    lateinit var compositeDisposable: CompositeDisposable
    //Сюда передаем ссылку на ЖЦ компонента, за которым будет слежение
    fun bindTo(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
        compositeDisposable = CompositeDisposable()
    }
    //Метод для добавления Observable в CompositeDisposable
    fun add(disposable: Disposable) {
        if (::compositeDisposable.isInitialized) {
            compositeDisposable.add(disposable)
        } else {
            throw NotImplementedError("must bind AutoDisposable to a Lifecycle first")
        }
    }
    //Этот аннотация позволяет вызывать метод по событию ЖЦ
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        compositeDisposable.dispose()
    }
}
//Экстеншн
fun Disposable.addTo(autoDisposable: AutoDisposable) {
    autoDisposable.add(this)
}