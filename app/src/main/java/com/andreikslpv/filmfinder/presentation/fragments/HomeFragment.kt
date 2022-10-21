package com.andreikslpv.filmfinder.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel
import com.andreikslpv.filmfinder.presentation.MainActivity
import com.andreikslpv.filmfinder.presentation.recyclers.AdRecyclerAdapter
import com.andreikslpv.filmfinder.presentation.recyclers.FilmListRecyclerAdapter
import com.andreikslpv.filmfinder.presentation.recyclers.itemDecoration.TopSpacingItemDecoration
import com.andreikslpv.filmfinder.presentation.recyclers.touchHelper.FilmTouchHelperCallback
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initAdRecycler()
        initFilmListRecycler()
        initSearchView()
    }

    override fun onResume() {
        super.onResume()
        filmsAdapter.changeItems((activity as MainActivity).filmsRepository.getAllFilmsWithFavoritesChecked())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    /*private fun initAdRecycler() {
        val adRecycler: RecyclerView = requireView().findViewById(R.id.ad_recycler)
        adRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adAdapter = AdRecyclerAdapter()
        adRecycler.adapter = adAdapter
        adAdapter.changeItems((activity as MainActivity).filmsRepository.getAd())
    }*/

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
        filmsAdapter.changeItems((activity as MainActivity).filmsRepository.getAllFilmsWithFavoritesChecked())
    }

    private fun initSearchView() {
        val searchView = requireView().findViewById<SearchView>(R.id.search_view)
        searchView.setOnClickListener {
            searchView.isIconified = false
        }
        //Подключаем слушателя изменений введенного текста в поиска
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            //Этот метод отрабатывает на каждое изменения текста
            override fun onQueryTextChange(newText: String): Boolean {
                //Если ввод пуст то вставляем в адаптер всю БД
                if (newText.isEmpty()) {
                    filmsAdapter.changeItems((activity as MainActivity).filmsRepository.getAllFilms())
                    return true
                }
                //Фильтруем список на поиск подходящих сочетаний
                val result = (activity as MainActivity).filmsRepository.getAllFilms().filter {
                    //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                    it.title.lowercase(Locale.getDefault()).contains(newText.lowercase(Locale.getDefault()))
                }
                //Добавляем в адаптер
                filmsAdapter.changeItems(result)
                return true
            }
        })
    }
    /*fun changeAd() {
        val adRecycler: RecyclerView = requireView().findViewById(R.id.ad_recycler)
        var i = adRecycler.getChildAdapterPosition(adRecycler.getChildAt(2))
        i++
        val adsCount = adRecycler.adapter?.itemCount
        if (adsCount != null) {
            if (i >= adsCount) i = 0
            adRecycler.scrollToPosition(i)
        }
    }*/

}