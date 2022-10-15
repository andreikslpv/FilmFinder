package com.andreikslpv.filmfinder.datasource.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilmsApiModel(
    val id: Int,
    val title: String,
    val poster: Int,
    val description: String,
    val descriptionFull: String
) : Parcelable