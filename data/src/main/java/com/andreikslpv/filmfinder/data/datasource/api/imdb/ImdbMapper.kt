package com.andreikslpv.filmfinder.data.datasource.api.imdb

import com.andreikslpv.filmfinder.domain.BaseMapper
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

object ImdbCategoryItemToDomainModel :
    BaseMapper<List<ImdbDtoCategoryItem?>, List<FilmDomainModel>> {
    override fun map(type: List<ImdbDtoCategoryItem?>?): List<FilmDomainModel> {
        return type?.map {
            FilmDomainModel(
                id = it?.id ?: "",
                title = it?.title ?: "",
                posterPreview = it?.image ?: "",
                posterDetails = it?.image ?: "",
                description = it?.crew ?: "",
                rating = it?.imDbRating.let { newRating ->
                    if (newRating.isNullOrBlank()) 0.0
                    else newRating.toDouble()
                },
                isFavorite = false,
                isWatchLater = false
            )
        } ?: listOf()
    }
}

object ImdbSearchItemToDomainModel : BaseMapper<List<ImdbDtoSearchItem>, List<FilmDomainModel>> {
    override fun map(type: List<ImdbDtoSearchItem>?): List<FilmDomainModel> {
        return type?.map {
            FilmDomainModel(
                id = it.id ?: "",
                title = it.title ?: "",
                posterPreview = it.image ?: "",
                posterDetails = it.image ?: "",
                description = it.description ?: "",
                rating = 0.0,
                isFavorite = false,
                isWatchLater = false
            )
        } ?: listOf()
    }
}