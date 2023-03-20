package com.andreikslpv.filmfinder

import android.app.Application
import com.andreikslpv.filmfinder.data.di.DaggerDataComponent
import com.andreikslpv.filmfinder.data.di.DatabaseModule
import com.andreikslpv.filmfinder.di.AppComponent
import com.andreikslpv.filmfinder.di.DaggerAppComponent
import com.andreikslpv.filmfinder.di.modules.AppModule
import com.andreikslpv.filmfinder.di.modules.DomainModule
import com.andreikslpv.filmfinder.remote_module.DaggerRemoteComponent

class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()

        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        //Создаем компонент
        //dagger = DaggerAppComponent.factory().create(this)
        val remoteProvider = DaggerRemoteComponent.create()
        val dataProvider = DaggerDataComponent.builder()
            .databaseModule(DatabaseModule(this))
            .remoteProvider(remoteProvider)
            .build()
        dagger = DaggerAppComponent.builder()
            .domainModule(DomainModule())
            .dataProvider(dataProvider)
            .appModule(AppModule(this))
            .build()
    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }
}