package com.andreikslpv.filmfinder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var posterCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.top_bar)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = CustomRecyclerAdapter(getImagesIdentifiers())

        initMenus()
    }

    private fun getImagesIdentifiers(): ArrayList<Int> {
        var resID: Int
        var imageNumber = 1
        val images = ArrayList<Int>()
        do {
            resID = resources.getIdentifier(
                "poster_$imageNumber",
                "drawable",
                packageName
            )
            if (resID != 0) images.add(resID)
            imageNumber++
        } while (resID != 0)
        posterCount = images.size
        return images
    }

    private fun changePosterAndToast(text: CharSequence) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        val recyclerView: RecyclerView = findViewById(R.id.top_bar)
        var i = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(2))
        i++
        if (i >= posterCount) i = 0
        recyclerView.scrollToPosition(i)
    }

    private fun initMenus() {
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    changePosterAndToast(it.title)
                    true
                }
                else -> false
            }
        }

        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorites, R.id.watch_later, R.id.selections -> {
                    changePosterAndToast(it.title)
                    true
                }
                else -> false
            }
        }
    }
}