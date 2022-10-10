package com.andreikslpv.filmfinder.presentation.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.domain.model.Film
import kotlin.math.roundToInt

const val DIVIDER_INTO_PARTS = 0.335

class AdRecyclerAdapter :
    RecyclerView.Adapter<AdViewHolder>() {

    private val items = mutableListOf<Film>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ad_item, parent, false)
        val layoutParams = itemView.layoutParams
        layoutParams.width = (parent.width * DIVIDER_INTO_PARTS).roundToInt()
        itemView.layoutParams = layoutParams

        return AdViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    //Метод для добавления объектов в наш список
    fun changeItems(list: List<Film>) {
        val diff = FilmDiff(items, list)
        val diffResult = DiffUtil.calculateDiff(diff)
        //Сначала очищаем
        items.clear()
        //Добавляем
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}