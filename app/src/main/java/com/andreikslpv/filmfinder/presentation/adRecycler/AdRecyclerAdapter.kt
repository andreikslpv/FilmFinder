package com.andreikslpv.filmfinder.presentation.adRecycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.filmfinder.R
import kotlin.math.roundToInt


class AdRecyclerAdapter(private val images: ArrayList<Int>) :
    RecyclerView.Adapter<AdRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posterImage: ImageView = itemView.findViewById(R.id.ad_poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ad_item, parent, false)
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