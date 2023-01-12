package com.andreikslpv.filmfinder.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class FilmDomainModel(
    val id: String,
    val title: String,
    val posterPreview: String,
    val posterDetails: String,
    val description: String,
    var rating: Double = 0.0,
    var isFavorite: Boolean = false,
    var isWatchLater: Boolean = false
) : Parcelable