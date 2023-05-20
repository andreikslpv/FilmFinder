package com.andreikslpv.filmfinder

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.andreikslpv.filmfinder.data.di.DaggerDataComponent
import com.andreikslpv.filmfinder.data.di.DataModule
import com.andreikslpv.filmfinder.database_module.DaggerDatabaseComponent
import com.andreikslpv.filmfinder.database_module.DatabaseModule
import com.andreikslpv.filmfinder.di.AppComponent
import com.andreikslpv.filmfinder.di.DaggerAppComponent
import com.andreikslpv.filmfinder.di.modules.AppModule
import com.andreikslpv.filmfinder.di.modules.DomainModule
import com.andreikslpv.filmfinder.presentation.notifications.NotificationConstants.CHANNEL_DESCRIPTION
import com.andreikslpv.filmfinder.presentation.notifications.NotificationConstants.CHANNEL_ID
import com.andreikslpv.filmfinder.presentation.notifications.NotificationConstants.CHANNEL_NAME
import com.andreikslpv.filmfinder.remote_module.DaggerRemoteComponent

class App : Application() {
    lateinit var dagger: AppComponent
    var isPromoShown = false

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Задаем имя, описание и важность канала
            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            //Создаем канал, передав в параметры его ID(строка), имя(строка), важность(константа)
            val notificationChannel = NotificationChannel(CHANNEL_ID, name, importance)
            //Отдельно задаем описание
            notificationChannel.description = descriptionText
            //Получаем доступ к менеджеру нотификаций
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            //Регистрируем канал
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }
}