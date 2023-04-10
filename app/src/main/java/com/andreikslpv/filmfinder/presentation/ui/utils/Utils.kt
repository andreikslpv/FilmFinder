package com.andreikslpv.filmfinder.presentation.ui.utils

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan
import java.text.SimpleDateFormat
import java.util.*

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

fun String.getOnlyDigital(): Int {
    val temp = this.filter {
        it in listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    }
    return try {
        temp.toInt()
    } catch (e: Exception) {
        0
    }
}

@Suppress("DEPRECATION")
fun Long.toTime(context: Context): String {
    val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
        context.resources.configuration.locales.get(0)
    } else{
        context.resources.configuration.locale
    }

    val date = Date(this)
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm", locale)
    return format.format(date)
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