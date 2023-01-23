package com.andreikslpv.filmfinder.data.datasource.local.models

import androidx.room.Embedded
import androidx.room.Relation
import com.andreikslpv.filmfinder.data.datasource.local.db.DbConstants

data class CategoryAndFilmModel(
    @Embedded
    val categoryModel: CategoryModel,

    @Relation(parentColumn = DbConstants.COLUMN_FILM, entityColumn = DbConstants.COLUMN_FILM_ID)
    val film:FilmLocalModel
)
