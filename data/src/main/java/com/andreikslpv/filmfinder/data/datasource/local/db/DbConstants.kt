package com.andreikslpv.filmfinder.data.datasource.local.db

object DbConstants {

    const val DATABASE_NAME = "film_db"
    const val TABLE_CACHED_FILMS = "cached_films"
    const val COLUMN_FILM_ID = "film_id"
    const val COLUMN_TITLE = "title"
    const val COLUMN_POSTER_PREVIEW = "poster_preview"
    const val COLUMN_POSTER_DETAILS = "poster_details"
    const val COLUMN_DESCRIPTION = "description"
    const val COLUMN_RATING = "rating"
    const val COLUMN_IS_FAVORITE = "is_favorite"
    const val COLUMN_IS_WATCH_LATER = "is_watch_later"

    const val TABLE_CACHED_CATEGORY = "cached_category"
    const val COLUMN_CATEGORY_ID = "category_id"
    const val COLUMN_API = "api"
    const val COLUMN_CATEGORY = "category"
    const val COLUMN_FILM = "film"

}