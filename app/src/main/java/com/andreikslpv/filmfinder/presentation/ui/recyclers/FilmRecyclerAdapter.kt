package com.andreikslpv.filmfinder.presentation.ui.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.filmfinder.databinding.ItemFilmBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.presentation.ui.TRANSITION_NAME_FOR_IMAGE
import com.andreikslpv.filmfinder.presentation.ui.TRANSITION_NAME_FOR_RATING
import com.andreikslpv.filmfinder.presentation.ui.TRANSITION_NAME_FOR_TEXT

//в параметр передаем слушатель, чтобы мы потом могли обрабатывать нажатия из класса Activity
class FilmRecyclerAdapter(private val clickListener: FilmOnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //Здесь у нас хранится список элементов для RV
    val items = mutableListOf<FilmDomainModel>()

    //Этот метод нужно переопределить на возврат количества элементов в списке RV
    override fun getItemCount() = items.size

    //В этом методе мы привязываем наш ViewHolder и передаем туда "надутую" верстку нашего фильма
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmViewHolder(
            ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    //В этом методе будет привязка полей из объекта Film к View из film_item.xml
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Проверяем какой у нас ViewHolder
        when (holder) {
            is FilmViewHolder -> {
                //Вызываем метод bind(), который мы создали, и передаем туда объект
                //из нашей базы данных с указанием позиции
                holder.bind(items[position])
                //Обрабатываем нажатие на весь элемент целиком(можно сделать на отдельный элемент
                //например, картинку) и вызываем метод нашего листенера, который мы получаем из
                //конструктора адаптера
                holder.binding.itemContainer.setOnClickListener {
                    holder.binding.poster.transitionName = TRANSITION_NAME_FOR_IMAGE
                    holder.binding.description.transitionName = TRANSITION_NAME_FOR_TEXT
                    holder.binding.ratingDonut.transitionName = TRANSITION_NAME_FOR_RATING
                    clickListener.click(
                        items[position],
                        holder.binding.poster,
                        holder.binding.description,
                        holder.binding.ratingDonut
                    )
                }
            }
        }
    }

    //Метод для добавления объектов в наш список
    fun changeItems(list: List<FilmDomainModel>) {
        val diff = FilmDiff(items, list)
        val diffResult = DiffUtil.calculateDiff(diff)
        //Сначала очищаем
        items.clear()
        //Добавляем
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

}