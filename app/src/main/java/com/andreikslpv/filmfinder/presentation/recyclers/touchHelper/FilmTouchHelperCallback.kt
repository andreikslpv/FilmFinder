package com.andreikslpv.filmfinder.presentation.recyclers.touchHelper

import android.annotation.SuppressLint
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.andreikslpv.filmfinder.presentation.recyclers.FilmListRecyclerAdapter
import java.util.*

class FilmTouchHelperCallback(private val adapter: FilmListRecyclerAdapter) :
    ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        //Drag & drop не поддерживается
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        //Swipe не поддерживается
        return false
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        //Настраиваем флаги для drag & drop и swipe жестов
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean {
        val items = adapter.items
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        //Меняем элементы местами с помощью метода swap
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }
        //Сообщаем об изменениях адаптеру
        adapter.notifyDataSetChanged()
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        adapter.items.removeAt(viewHolder.adapterPosition)
        adapter.notifyDataSetChanged()
    }
}