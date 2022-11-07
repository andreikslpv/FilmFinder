package com.andreikslpv.filmfinder.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.filmfinder.databinding.FragmentHomeBinding
import com.andreikslpv.filmfinder.domain.models.FilmsLocalModel
import com.andreikslpv.filmfinder.presentation.AnimationHelper
import com.andreikslpv.filmfinder.presentation.MainActivity
import com.andreikslpv.filmfinder.presentation.recyclers.FilmListRecyclerAdapter
import com.andreikslpv.filmfinder.presentation.recyclers.itemDecoration.TopSpacingItemDecoration
import com.andreikslpv.filmfinder.presentation.recyclers.touchHelper.FilmTouchHelperCallback
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AnimationHelper.performFragmentCircularRevealAnimation(requireView(), requireActivity(), 1)

        initFilmListRecycler()
        initSearchView()
    }

    override fun onPause() {
        super.onPause()
        // меняем background MainActivity на background фрагмента
        (activity as MainActivity).setBackground(binding.homeFragmentRoot.background)
    }

    private fun initFilmListRecycler() {
        binding.homeRecycler.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: FilmsLocalModel, image: ImageView, text: TextView) {
                        (requireActivity() as MainActivity).launchDetailsFragment(
                            (activity as MainActivity).filmsRepository.getFilmLocalState(film),
                            image,
                            text
                        )
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
        filmsAdapter.changeItems((activity as MainActivity).filmsRepository.getAllFilms())
    }

    private fun initSearchView() {
        binding.homeSearchView.setOnClickListener {
            binding.homeSearchView.isIconified = false
        }
        //Подключаем слушателя изменений введенного текста в поиска
        binding.homeSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            //Этот метод отрабатывает на каждое изменения текста
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String): Boolean {
                //Если ввод пуст то вставляем в адаптер всю БД
                if (newText.isEmpty()) {
                    filmsAdapter.changeItems((activity as MainActivity).filmsRepository.getAllFilms())
                    return true
                }
                //Фильтруем список на поиск подходящих сочетаний
                val result = (activity as MainActivity).filmsRepository.getAllFilms().filter {
                    //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                    it.title.lowercase(Locale.getDefault())
                        .contains(newText.lowercase(Locale.getDefault()))
                }
                //Добавляем в адаптер
                filmsAdapter.changeItems(result)
                filmsAdapter.notifyDataSetChanged()
                return true
            }
        })
    }

}