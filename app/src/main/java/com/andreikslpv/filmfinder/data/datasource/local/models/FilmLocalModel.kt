package com.andreikslpv.filmfinder.data.datasource.local.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilmLocalModel (
    val id: Int,
    val title: String,
    val poster: Int,
    val description: String,
    var isFavorite: Boolean = false,
    var isWatchLater: Boolean = false
) : Parcelable