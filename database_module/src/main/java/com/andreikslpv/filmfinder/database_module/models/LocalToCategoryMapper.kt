package com.andreikslpv.filmfinder.database_module.models

object LocalToCategoryMapper {
    fun map(
        api: String,
        category: String,
        input: List<FilmLocalModel>,
        currentIndex: Int
    ): List<CategoryModel> {
        val result = mutableListOf<CategoryModel>()
        var i = currentIndex
        for (entity in input) {
            result.add(
                CategoryModel(
                    api = api,
                    category = category,
                    rank = i,
                    filmId = entity.id
                )
            )
            i++
        }
        return result
    }
}