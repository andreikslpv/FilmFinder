package com.andreikslpv.filmfinder.data.datasource.api

import com.andreikslpv.filmfinder.domain.ApiCallback

interface FilmsApiDataSource {

    fun getFilmsFromApi(page: Int, language: String, callback: ApiCallback)

    fun getSearchResultFromApi(query: String, page: Int, language: String, callback: ApiCallback)

}