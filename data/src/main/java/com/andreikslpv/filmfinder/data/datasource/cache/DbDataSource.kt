package com.andreikslpv.filmfinder.data.datasource.cache

import android.content.ContentValues
import androidx.paging.PagingSource
import com.andreikslpv.filmfinder.data.datasource.local.db.DatabaseHelper
import com.andreikslpv.filmfinder.data.datasource.local.db.DatabasePrinter
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import javax.inject.Inject

class DbDataSource @Inject constructor(private val databaseHelper: DatabaseHelper) :
    FilmsCacheDataSource {

    override fun saveFilmsToCache(
        films: List<FilmDomainModel>,
        api: ValuesType,
        category: CategoryType
    ) {
        val sqlDb = databaseHelper.readableDatabase
        val contentValues = ContentValues()
        for (item in films) {
            with(contentValues) {
                put(DatabaseHelper.COLUMN_API, api.value)
                put(DatabaseHelper.COLUMN_CATEGORY, category.name)
                put(DatabaseHelper.COLUMN_FILM_ID, item.id)
                put(DatabaseHelper.COLUMN_TITLE, item.title)
                put(DatabaseHelper.COLUMN_POSTER_PREVIEW, item.posterPreview)
                put(DatabaseHelper.COLUMN_POSTER_DETAILS, item.posterDetails)
                put(DatabaseHelper.COLUMN_DESCRIPTION, item.description)
                put(DatabaseHelper.COLUMN_RATING, item.rating)
            }
            sqlDb.insert(DatabaseHelper.TABLE_CACHE, null, contentValues)
        }
        sqlDb.close()
    }

    override fun deleteCachedFilms(api: ValuesType, category: CategoryType) {
        val sqlDb = databaseHelper.writableDatabase
        sqlDb.delete(
            DatabaseHelper.TABLE_CACHE,
            DatabaseHelper.COLUMN_API + " = ? AND " + DatabaseHelper.COLUMN_CATEGORY + " = ?",
            arrayOf(api.value, category.name)
        )
        sqlDb.close()
    }

    override fun deleteAllCachedFilms() {
        val sqlDb = databaseHelper.writableDatabase
        sqlDb.delete(DatabaseHelper.TABLE_CACHE, DatabaseHelper.COLUMN_ID + " > ?", arrayOf("-1"))
        sqlDb.close()
    }

    override fun getFilmsByCategoryPagingSource(
        api: ValuesType,
        category: CategoryType
    ): PagingSource<Int, FilmDomainModel> {
        return CachePagingSource(
            databaseHelper,
            "SELECT * FROM ${DatabaseHelper.TABLE_CACHE} "
                    + "WHERE ${DatabaseHelper.COLUMN_API} = ? AND ${DatabaseHelper.COLUMN_CATEGORY} = ?",
            arrayOf(api.value, category.name)
        )
    }

    override fun getSearchResultPagingSource(
        api: ValuesType,
        query: String
    ): PagingSource<Int, FilmDomainModel> {
        return CachePagingSource(
            databaseHelper,
            "SELECT * FROM ${DatabaseHelper.TABLE_CACHE} "
                    + "WHERE ${DatabaseHelper.COLUMN_API} = ? AND ${DatabaseHelper.COLUMN_TITLE} like ?",
            arrayOf(api.value, "%$query%")
        )
    }
}