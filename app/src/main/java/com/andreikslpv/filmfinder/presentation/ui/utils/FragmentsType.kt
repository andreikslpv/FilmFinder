package com.andreikslpv.filmfinder.presentation.ui.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class FragmentsType(val tag: String) : Parcelable {
    HOME("home"),
    FAVORITES("favorites"),
    WATCH_LATER("watch_later"),
    SELECTIONS("selections"),
    DETAILS("details"),
    NONE("none")
}