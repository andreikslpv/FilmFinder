package com.andreikslpv.filmfinder.presentation.ui.utils

import com.andreikslpv.filmfinder.R

enum class CategoryType(val tmdbPath: String, val res: Int) {
    NOW_PLAYING("now_playing", R.string.tmdb_now_playing),
    POPULAR("popular", R.string.tmdb_popular),
    TOP_RATED("top_rated", R.string.tmdb_top_rated),
    UPCOMING("upcoming", R.string.tmdb_upcoming);
}