package com.andreikslpv.filmfinder.data.datasource.local.db

import android.annotation.SuppressLint
import android.database.Cursor

class DatabasePrinter(private val databaseHelper: DatabaseHelper) {
    fun printDb(message: String = "") {
        val sqlDb = databaseHelper.readableDatabase
        val cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_CACHE} ORDER BY ${DatabaseHelper.COLUMN_API} AND ${DatabaseHelper.COLUMN_CATEGORY};", null)
        cursor.moveToFirst()
        cursorToString(cursor, message)
        cursor.close()
        sqlDb.close()
    }

    @SuppressLint("Range")
    private fun cursorToString(cursor: Cursor, message: String = "") {
        println("!!!--------------------------------$message--------------------------------")
        var headers = "!!!"
        var line = "!!!"
        if (cursor.moveToFirst()) {
            val columnNames = cursor.columnNames
            for (name in columnNames) headers += String.format("%s | ", name)
            println(headers)
            do {
                for (name in columnNames) {
                    line += String.format(
                        "%s | ",
                        cursor.getString(cursor.getColumnIndex(name))
                    )
                }
                println(line)
                line = "!!!"
            } while (cursor.moveToNext())
        }
    }
}