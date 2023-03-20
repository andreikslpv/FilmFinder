package com.andreikslpv.filmfinder

import android.app.Application
import com.andreikslpv.filmfinder.data.di.DaggerDataComponent
import com.andreikslpv.filmfinder.data.di.DataModule
import com.andreikslpv.filmfinder.database_module.DaggerDatabaseComponent
import com.andreikslpv.filmfinder.database_module.DatabaseModule
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
        //Создаем dagger компоненты
        val remoteProvider = DaggerRemoteComponent.create()

        val databaseProvider = DaggerDatabaseComponent.builder()
            .databaseModule(DatabaseModule(this))
            .build()

        val dataProvider = DaggerDataComponent.builder()
            .dataModule(DataModule(this))
            .databaseProvider(databaseProvider)
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