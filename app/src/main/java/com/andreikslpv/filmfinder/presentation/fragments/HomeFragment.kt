package com.andreikslpv.filmfinder.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.datasource.FilmsApiDataSource
import com.andreikslpv.filmfinder.datasource.FilmsCacheDataSource
import com.andreikslpv.filmfinder.datasource.FilmsLocalDataSource
import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel
import com.andreikslpv.filmfinder.presentation.MainActivity
import com.andreikslpv.filmfinder.presentation.recyclers.AdRecyclerAdapter
import com.andreikslpv.filmfinder.presentation.recyclers.FilmListRecyclerAdapter
import com.andreikslpv.filmfinder.presentation.recyclers.itemDecoration.TopSpacingItemDecoration
import com.andreikslpv.filmfinder.presentation.recyclers.touchHelper.FilmTouchHelperCallback
import com.andreikslpv.filmfinder.repository.FilmsRepositoryImpl
import com.google.gson.Gson
import java.io.File

class HomeFragment : Fragment() {
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private val filmsRepositoryImpl = FilmsRepositoryImpl(
        FilmsCacheDataSource(),
        FilmsApiDataSource(),
        FilmsLocalDataSource(File("local.json"), Gson())
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdRecycler()
        initFilmListRecycler()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun initAdRecycler() {
        val adRecycler: RecyclerView = requireView().findViewById(R.id.ad_recycler)
        adRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adAdapter = AdRecyclerAdapter()
        adRecycler.adapter = adAdapter
        adAdapter.changeItems(filmsRepositoryImpl.getAd())
    }

    private fun initFilmListRecycler() {
        val filmListRecycler = requireView().findViewById<RecyclerView>(R.id.film_list_recycler)
        filmListRecycler.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: FilmsLocalModel) {
                        (requireActivity() as MainActivity).launchDetailsFragment(film)
                    }
                })
            //Присваиваем адаптер
            adapter = filmsAdapter
            //Присвои layoutManager
            layoutManager = LinearLayoutManager(requireContext())
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
            val callback = FilmTouchHelperCallback(adapter as FilmListRecyclerAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(this)
        }
        //Кладем нашу БД в RV
        filmsAdapter.changeItems(filmsRepositoryImpl.getAllFilms())
    }

    fun changeAd() {
        val adRecycler: RecyclerView = requireView().findViewById(R.id.ad_recycler)
        var i = adRecycler.getChildAdapterPosition(adRecycler.getChildAt(2))
        i++
        val adsCount = adRecycler.adapter?.itemCount
        if (adsCount != null) {
            if (i >= adsCount) i = 0
            adRecycler.scrollToPosition(i)
        }
    }

}