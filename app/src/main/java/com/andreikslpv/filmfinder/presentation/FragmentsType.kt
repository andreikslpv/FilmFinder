package com.andreikslpv.filmfinder.presentation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class FragmentsType(val tag: String) : Parcelable {
    HOME("home"),
    FAVORITES("favorites"),
    WATCH_LATER("watch_later"),
    SELECTIONS("selections"),
    DETAILS("details")
}