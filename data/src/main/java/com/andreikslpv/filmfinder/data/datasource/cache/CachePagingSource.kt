package com.andreikslpv.filmfinder.data.datasource.cache

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andreikslpv.filmfinder.data.datasource.local.db.DatabaseHelper
import com.andreikslpv.filmfinder.data.repository.PAGE_SIZE
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel

const val START_PAGE = 0

class CachePagingSource(
    databaseHelper: DatabaseHelper,
    private val sqlQuery: String,
    private val arrayArgs: Array<String>
) : PagingSource<Int, FilmDomainModel>() {
    private val step = 1
    //Инициализируем объект для взаимодействия с БД
    private val sqlDb = databaseHelper.readableDatabase

    override fun getRefreshKey(state: PagingState<Int, FilmDomainModel>): Int? {
        // Самый последний доступный индекс в списке
        val anchorPosition = state.anchorPosition ?: return null
        // преобразуем item index в page index
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        // page не имеет значения "текущее", поэтому вычисляем сами
        return page.prevKey?.plus(step) ?: page.nextKey?.minus(step)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmDomainModel> {
        try {
            val pageNumber = params.key ?: START_PAGE
            //Создаем курсор на основании запроса "Получить из таблицы записи для указанных апи и категории"
            val cursor = sqlDb.rawQuery(sqlQuery, arrayArgs)
            val totalPages = cursor.count / PAGE_SIZE
            //Сюда будем сохранять результат получения данных
            val result = mutableListOf<FilmDomainModel>()
            //Проверяем, есть ли хоть одна строка в ответе на запрос
            if (cursor.moveToFirst()) {
                //Итерируемся по таблице, пока есть записи, и создаем на основании объект Film
                do {
                    val id = cursor.getString(3)
                    val title = cursor.getString(4)
                    val posterPreview = cursor.getString(5)
                    val posterDetails = cursor.getString(6)
                    val description = cursor.getString(7)
                    val rating = cursor.getDouble(8)

                    result.add(
                        FilmDomainModel(
                            id = id,
                            title = title,
                            posterPreview = posterPreview,
                            posterDetails = posterDetails,
                            description = description,
                            rating = rating,
                        )
                    )
                } while (cursor.moveToNext())
            }
            cursor.close()
            return LoadResult.Page(
                data = result,
                prevKey = if (pageNumber > START_PAGE) pageNumber - step else null,
                nextKey = if (pageNumber < totalPages) pageNumber + step else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}