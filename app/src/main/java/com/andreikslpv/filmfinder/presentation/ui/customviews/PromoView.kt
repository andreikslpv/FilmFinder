package com.andreikslpv.filmfinder.presentation.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.andreikslpv.filmfinder.databinding.MergePromoBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class PromoView(context: Context, attributeSet: AttributeSet?) :
    FrameLayout(context, attributeSet) {
    val binding = MergePromoBinding.inflate(LayoutInflater.from(context), this)
    val closeButton = binding.closeButton
    val poster = binding.poster
    private val cornerRadius = 55

    fun setLinkForPoster(posterUrl: String) {
        Glide.with(binding.root)
            .load(posterUrl)
            .apply(
                RequestOptions().transform(
                    CenterCrop(),
                    RoundedCorners(cornerRadius)
                )
            )
            .into(binding.poster)
    }
}