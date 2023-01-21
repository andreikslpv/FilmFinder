package com.andreikslpv.filmfinder.data.datasource.local.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        //Создаем саму таблицу для фильмов
        db?.execSQL(
            "CREATE TABLE $TABLE_CACHE ("
                    + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "$COLUMN_API TEXT, "
                    + "$COLUMN_CATEGORY TEXT, "
                    + "$COLUMN_FILM_ID TEXT, "
                    + "$COLUMN_TITLE TEXT, "
                    + "$COLUMN_POSTER_PREVIEW TEXT, "
                    + "$COLUMN_POSTER_DETAILS TEXT, "
                    + "$COLUMN_DESCRIPTION TEXT, "
                    + "$COLUMN_RATING REAL);"
        )
    }

    //Миграций мы не предполагаем, поэтому метод пустой
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    companion object {
        //Название самой БД
        private const val DATABASE_NAME = "films.db"

        //Версия БД
        private const val DATABASE_VERSION = 1

        //Константы для работы с таблицей, они нам понадобятся в CRUD операциях и,
        //возможно, в составлении запросов
        const val TABLE_CACHE = "table_cache"
        const val COLUMN_ID = "_id"
        const val COLUMN_API = "api"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_FILM_ID = "film_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_POSTER_PREVIEW = "poster_preview"
        const val COLUMN_POSTER_DETAILS = "poster_details"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_RATING = "rating"
    }
}