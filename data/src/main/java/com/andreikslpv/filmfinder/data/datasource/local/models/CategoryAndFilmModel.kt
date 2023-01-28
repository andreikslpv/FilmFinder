package com.andreikslpv.filmfinder.data.datasource.local.models

import androidx.room.Embedded
import androidx.room.Relation
import com.andreikslpv.filmfinder.data.datasource.local.db.RoomConstants

data class CategoryAndFilmModel(
    @Embedded
    val categoryModel: CategoryModel,

    @Relation(parentColumn = RoomConstants.COLUMN_FILM, entityColumn = RoomConstants.COLUMN_FILM_ID)
    val film:FilmLocalModel
)
