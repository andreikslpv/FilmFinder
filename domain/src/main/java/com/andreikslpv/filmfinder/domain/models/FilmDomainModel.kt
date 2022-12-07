package com.andreikslpv.filmfinder.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class FilmDomainModel(
    val id: Int,
    val title: String,
    val poster: Int,
    val description: String,
    var rating: Float = 0f,
    var isFavorite: Boolean = false,
    var isWatchLater: Boolean = false
) : Parcelable