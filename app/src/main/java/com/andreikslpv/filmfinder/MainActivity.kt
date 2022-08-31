package com.andreikslpv.filmfinder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMenuButtons()
    }

    private fun initMenuButtons() {
        button_menu.setOnClickListener {
            Toast.makeText(this, button_menu.text, Toast.LENGTH_SHORT).show()
        }
        button_collections.setOnClickListener {
            Toast.makeText(this, button_collections.text, Toast.LENGTH_SHORT).show()
        }
        button_favourites.setOnClickListener {
            Toast.makeText(this, button_favourites.text, Toast.LENGTH_SHORT).show()
        }
        button_see_later.setOnClickListener {
            Toast.makeText(this, button_see_later.text, Toast.LENGTH_SHORT).show()
        }
        button_settings.setOnClickListener {
            Toast.makeText(this, button_settings.text, Toast.LENGTH_SHORT).show()
        }
    }
}