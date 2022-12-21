package com.andreikslpv.filmfinder.data.datasource.api.tmdb

import com.andreikslpv.filmfinder.data.BuildConfig
import com.andreikslpv.filmfinder.data.datasource.api.FilmsApiDataSource
import com.andreikslpv.filmfinder.domain.ApiCallback
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class FilmsTmdbDataSource : FilmsApiDataSource {
    private var retrofitServiceGetPopular: TmdbInterfaceGetPopular
    private var retrofitServiceGetSearchResult: TmdbInterfaceGetSearchResult

    init {
        //Создаём кастомный клиент
        val okHttpClient = OkHttpClient.Builder()
            //Настраиваем таймауты для медленного интернета
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            //Добавляем логгер
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()
        //Создаем Ретрофит
        val retrofit = Retrofit.Builder()
            //Указываем базовый URL из констант
            .baseUrl(TmdbConstants.BASE_URL)
            //Добавляем конвертер
            .addConverterFactory(GsonConverterFactory.create())
            //Добавляем кастомный клиент
            .client(okHttpClient)
            .build()
        //Создаем сам сервис с методами для запросов
        retrofitServiceGetPopular = retrofit.create(TmdbInterfaceGetPopular::class.java)
        retrofitServiceGetSearchResult = retrofit.create(TmdbInterfaceGetSearchResult::class.java)
    }

    override fun getFilmsFromApi(page: Int, language: String, callback: ApiCallback) {
        retrofitServiceGetPopular.getFilms(
            TmdbConstants.VERSION_API,
            TmdbConstants.PATH_GET_POPULAR_1,
            TmdbConstants.PATH_GET_POPULAR_2,
            TmdbKey.KEY,
            language,
            page
        ).enqueue(object : Callback<TmdbDtoResults> {
                override fun onResponse(
                    call: Call<TmdbDtoResults>,
                    response: Response<TmdbDtoResults>
                ) {
                    //При успехе мы вызываем метод onSuccess и передаем в этот коллбэк список фильмов
                    callback.onSuccess(TmdbToDomainModel.map(response.body()?.tmdbDtoFilms))
                }

                override fun onFailure(call: Call<TmdbDtoResults>, t: Throwable) {
                    //В случае провала вызываем другой метод коллбека
                    callback.onFailure(t.message?:"")
                }
            })
    }

    override fun getSearchResultFromApi(
        query: String,
        page: Int,
        language: String,
        callback: ApiCallback
    ) {
        retrofitServiceGetSearchResult.getFilms(
            TmdbConstants.VERSION_API,
            TmdbConstants.PATH_GET_SEARCH_RESULT_1,
            TmdbConstants.PATH_GET_SEARCH_RESULT_2,
            TmdbKey.KEY,
            query,
            language,
            page
        ).enqueue(object : Callback<TmdbDtoResults> {
                override fun onResponse(
                    call: Call<TmdbDtoResults>,
                    response: Response<TmdbDtoResults>
                ) {
                    //При успехе мы вызываем метод onSuccess и передаем в этот коллбэк список фильмов
                    callback.onSuccess(TmdbToDomainModel.map(response.body()?.tmdbDtoFilms))
                }

                override fun onFailure(call: Call<TmdbDtoResults>, t: Throwable) {
                    //В случае провала вызываем другой метод коллбека
                    callback.onFailure(t.message?:"")
                }
            })
    }


}