package com.andreikslpv.filmfinder.remote_module

import com.andreikslpv.filmfinder.remote_module.imdb.ImdbConstants
import com.andreikslpv.filmfinder.remote_module.imdb.ImdbServiceFilmsByCategory
import com.andreikslpv.filmfinder.remote_module.imdb.ImdbServiceSearchResult
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbConstants
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbServiceFilmsByCategory
import com.andreikslpv.filmfinder.remote_module.tmdb.TmdbServiceSearchResult
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

const val TIMEOUT = 30L

@Module
class RemoteModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        //Настраиваем таймауты для медленного интернета
        .callTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        //Добавляем логгер
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        })
        .build()

    @Provides
    @Singleton
    @TmdbRetrofitQualifier
    fun provideTmdbRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        //Указываем базовый URL из констант
        .baseUrl(TmdbConstants.BASE_URL)
        //Добавляем конвертер
        .addConverterFactory(GsonConverterFactory.create())
        //Добавляем поддержку RxJava
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        //Добавляем кастомный клиент
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideTmdbServiceFilmsByCategory(@TmdbRetrofitQualifier retrofit: Retrofit): TmdbServiceFilmsByCategory =
        retrofit.create(TmdbServiceFilmsByCategory::class.java)

    @Provides
    @Singleton
    fun provideTmdbServiceSearchResult(@TmdbRetrofitQualifier retrofit: Retrofit): TmdbServiceSearchResult =
        retrofit.create(TmdbServiceSearchResult::class.java)

    @Provides
    @Singleton
    @ImdbRetrofitQualifier
    fun provideImdbRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        //Указываем базовый URL из констант
        .baseUrl(ImdbConstants.BASE_URL)
        //Добавляем конвертер
        .addConverterFactory(GsonConverterFactory.create())
        //Добавляем поддержку RxJava
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        //Добавляем кастомный клиент
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideImdbServiceFilmsByCategory(@ImdbRetrofitQualifier retrofit: Retrofit): ImdbServiceFilmsByCategory =
        retrofit.create(ImdbServiceFilmsByCategory::class.java)

    @Provides
    @Singleton
    fun provideImdbServiceSearchResult(@ImdbRetrofitQualifier retrofit: Retrofit): ImdbServiceSearchResult =
        retrofit.create(ImdbServiceSearchResult::class.java)

}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TmdbRetrofitQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ImdbRetrofitQualifier