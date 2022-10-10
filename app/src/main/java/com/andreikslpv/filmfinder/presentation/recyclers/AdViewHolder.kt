package com.andreikslpv.filmfinder.presentation.recyclers

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.domain.model.Film

class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val posterImage: ImageView = itemView.findViewById(R.id.ad_poster)

    fun bind(film: Film) {
        posterImage.setImageResource(film.poster)
    }
}