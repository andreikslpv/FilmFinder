package com.andreikslpv.filmfinder.data.datasource.api.kp

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

private const val TYPE_FIELD = "typeNumber"
private const val TYPE_SEARCH = "1"
private const val NAME_FIELD = "name"
private const val IS_STRICT = false


class FilmsKpDataSource : FilmsApiDataSource {
    private var retrofitServiceGetCategory: KpInterfaceGetCategory
    private var retrofitServiceGetSearchResult: KpInterfaceGetSearchResult

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
            .baseUrl(KpConstants.BASE_URL)
            //Добавляем конвертер
            .addConverterFactory(GsonConverterFactory.create())
            //Добавляем кастомный клиент
            .client(okHttpClient)
            .build()
        //Создаем сам сервис с методами для запросов
        retrofitServiceGetCategory = retrofit.create(KpInterfaceGetCategory::class.java)
        retrofitServiceGetSearchResult = retrofit.create(KpInterfaceGetSearchResult::class.java)
    }

    override fun getFilmsFromApi(page: Int, language: String, callback: ApiCallback) {
        retrofitServiceGetCategory.getFilms(
            KpConstants.PATH_GET_MOVIE,
            TYPE_SEARCH,
            TYPE_FIELD,
            KpKey.KEY
        ).enqueue(object : Callback<KpDtoResults> {
            override fun onResponse(
                call: Call<KpDtoResults>,
                response: Response<KpDtoResults>
            ) {
                //При успехе мы вызываем метод onSuccess и передаем в этот коллбэк список фильмов
                callback.onSuccess(KpToDomainModel.map(response.body()?.docs))
            }

            override fun onFailure(call: Call<KpDtoResults>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure(t.message ?: "")
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
            KpConstants.PATH_GET_MOVIE,
            query,
            NAME_FIELD,
            IS_STRICT,
            KpKey.KEY
        ).enqueue(object : Callback<KpDtoResults> {
            override fun onResponse(
                call: Call<KpDtoResults>,
                response: Response<KpDtoResults>
            ) {
                //При успехе мы вызываем метод onSuccess и передаем в этот коллбэк список фильмов
                callback.onSuccess(KpToDomainModel.map(response.body()?.docs))
            }

            override fun onFailure(call: Call<KpDtoResults>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure(t.message ?: "")
            }
        })
    }


}