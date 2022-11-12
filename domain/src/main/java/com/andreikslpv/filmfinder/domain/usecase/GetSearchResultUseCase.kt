package com.andreikslpv.filmfinder.domain.usecase

import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import java.util.*

class GetSearchResultUseCase {
    fun execute(searchText: String, listToSearch: List<FilmDomainModel>): List<FilmDomainModel> {
        //Если текст для поиска пуст то возвращаем список без изменений
        if (searchText.isEmpty()) {
            return listToSearch
        }
        //Фильтруем список на поиск подходящих сочетаний
        val result = listToSearch.filter {
            //Чтобы все работало правильно, и запрос, и имя фильма приводим к нижнему регистру
            it.title.lowercase(Locale.getDefault())
                .contains(searchText.lowercase(Locale.getDefault()))
        }
        return result
    }
}