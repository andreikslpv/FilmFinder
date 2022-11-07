package com.andreikslpv.filmfinder.presentation.recyclers

import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.filmfinder.databinding.ItemFilmBinding
import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel
import com.bumptech.glide.Glide

//В конструктор класса передается layout, который мы создали(film_item.xml)
class FilmViewHolder(val binding: ItemFilmBinding) : RecyclerView.ViewHolder(binding.root) {

    //В этом методе кладем данные из Film в наши View
    fun bind(film: FilmsLocalModel) {
        //Устанавливаем заголовок
        binding.title.text = film.title
        //Устанавливаем постер
        //Указываем контейнер, в котором будет "жить" наша картинка
        Glide.with(itemView)
            //Загружаем сам ресурс
            .load(film.poster)
            //Центруем изображение
            .centerCrop()
            //Указываем ImageView, куда будем загружать изображение
            .into(binding.poster)
        //Устанавливаем описание
        binding.description.text = film.description
    }
}