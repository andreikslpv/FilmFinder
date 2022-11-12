package com.andreikslpv.filmfinder.data.datasource.api.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilmApiModel(
    val id: Int,
    val title: String,
    val poster: Int,
    val description: String
) : Parcelable