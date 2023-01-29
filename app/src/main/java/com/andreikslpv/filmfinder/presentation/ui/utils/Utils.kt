package com.andreikslpv.filmfinder.presentation.ui.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun ImageView.loadImage(url: String) {
    if (url.isNotBlank())
        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(this)
}

fun String.makeToast(context: Context) {
    Toast.makeText(
        context,
        this,
        Toast.LENGTH_SHORT
    ).show()
}


// CustomFlowOperator
/* Emits the previous values ('null' if there is no previous values) along with the current one.
For example:
- original flow:
"a", "b", "c" ...
- resulting flow (count = 2):
(null, null), (null, "a"), ("a", "b"), ("b", "c"), ...
 */
fun <T> Flow<T>.simpleScan(count: Int): Flow<List<T?>> {
    val items = List<T?>(count) { null }
    return this.scan(items) { previous, value -> previous.drop(1) + value }
}