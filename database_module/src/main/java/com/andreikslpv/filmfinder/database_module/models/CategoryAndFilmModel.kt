package com.andreikslpv.filmfinder.database_module.models

import androidx.room.Embedded
import androidx.room.Relation
import com.andreikslpv.filmfinder.database_module.db.RoomConstants

data class CategoryAndFilmModel(
    @Embedded
    val categoryModel: CategoryModel,

    @Relation(parentColumn = RoomConstants.COLUMN_FILM, entityColumn = RoomConstants.COLUMN_FILM_ID)
    val film:FilmLocalModel
)
