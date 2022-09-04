package com.andreikslpv.filmfinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt


class CustomRecyclerAdapter(private val images: ArrayList<Int>) :
    RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posterImage: ImageView = itemView.findViewById(R.id.poster_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
        val layoutParams = itemView.layoutParams
        layoutParams.width = (parent.width * 0.335).roundToInt()
        itemView.layoutParams = layoutParams

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.posterImage.setImageResource(images[position])
    }

    override fun getItemCount() = images.size
}