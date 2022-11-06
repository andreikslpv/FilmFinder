package com.andreikslpv.filmfinder.presentation.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel
import com.andreikslpv.filmfinder.presentation.TRANSITION_NAME_FOR_IMAGE
import com.andreikslpv.filmfinder.presentation.TRANSITION_NAME_FOR_TEXT

//в параметр передаем слушатель, чтобы мы потом могли обрабатывать нажатия из класса Activity
class FilmListRecyclerAdapter(private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //Здесь у нас хранится список элементов для RV
    val items = mutableListOf<FilmsLocalModel>()

    //Этот метод нужно переопределить на возврат количества элементов в списке RV
    override fun getItemCount() = items.size

    //В этом методе мы привязываем наш ViewHolder и передаем туда "надутую" верстку нашего фильма
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
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
                val itemContainer = holder.itemView.findViewById<CardView>(R.id.item_container)
                itemContainer.setOnClickListener {
                    val image = holder.itemView.findViewById<ImageView>(R.id.poster)
                    val text = holder.itemView.findViewById<TextView>(R.id.description)
                    image.transitionName = TRANSITION_NAME_FOR_IMAGE
                    text.transitionName = TRANSITION_NAME_FOR_TEXT
                    clickListener.click(items[position], image, text)
                }
            }
        }
    }

    //Метод для добавления объектов в наш список
    fun changeItems(list: List<FilmsLocalModel>) {
        val diff = FilmDiff(items, list)
        val diffResult = DiffUtil.calculateDiff(diff)
        //Сначала очищаем
        items.clear()
        //Добавляем
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }


    //Интерфейс для обработки кликов
    interface OnItemClickListener {
        fun click(film: FilmsLocalModel, image: ImageView, text: TextView)
    }
}