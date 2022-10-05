package com.andreikslpv.filmfinder.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(
    val title: String,
    val poster: Int,
    val description: String,
    val descriptionFull: String
) : Parcelable