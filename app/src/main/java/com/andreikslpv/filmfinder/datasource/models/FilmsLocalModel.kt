package com.andreikslpv.filmfinder.datasource.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FilmsLocalModel(
    val id: Int,
    val title: String,
    val poster: Int,
    val description: String,
    val descriptionFull: String,
    var isFavorite: Boolean = false,
    var isWatchLater: Boolean = false
) : Parcelable