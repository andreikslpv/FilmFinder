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
import com.andreikslpv.filmfinder.databinding.FragmentFavoritesBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.GetFavoritesFilmsUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetFilmLocalStateUseCase
import com.andreikslpv.filmfinder.domain.usecase.GetSearchResultUseCase
import com.andreikslpv.filmfinder.presentation.AnimationHelper
import com.andreikslpv.filmfinder.presentation.MainActivity
import com.andreikslpv.filmfinder.presentation.recyclers.FilmListRecyclerAdapter
import com.andreikslpv.filmfinder.presentation.recyclers.itemDecoration.TopSpacingItemDecoration
import com.andreikslpv.filmfinder.presentation.recyclers.touchHelper.FilmTouchHelperCallback
import com.andreikslpv.filmfinder.presentation.views.RatingDonutView

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding!!
    private val getFavoritesFilmsUseCase by lazy {
        GetFavoritesFilmsUseCase((activity as MainActivity).filmsRepository)
    }
    private val getFilmLocalStateUseCase by lazy {
        GetFilmLocalStateUseCase((activity as MainActivity).filmsRepository)
    }
    private val getSearchResultUseCase by lazy { GetSearchResultUseCase() }
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AnimationHelper.performFragmentCircularRevealAnimation(requireView(), requireActivity(), 2)

        initFilmListRecycler()
        initSearchView()
    }

    override fun onPause() {
        super.onPause()
        // меняем background MainActivity на background фрагмента
        (activity as MainActivity).setBackground(binding.favoritesFragmentRoot.background)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initFilmListRecycler() {
        binding.favoritesRecycler.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(
                        film: FilmDomainModel,
                        image: ImageView,
                        text: TextView,
                        rating: RatingDonutView
                    ) {
                        (requireActivity() as MainActivity).launchDetailsFragment(
                            getFilmLocalStateUseCase.execute(film),
                            image,
                            text,
                            rating
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
        filmsAdapter.changeItems(getFavoritesFilmsUseCase.execute())
    }

    private fun initSearchView() {
        binding.favoritesSearchView.setOnClickListener {
            binding.favoritesSearchView.isIconified = false
        }
        //Подключаем слушателя изменений введенного текста в поиска
        binding.favoritesSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            //Этот метод отрабатывает на каждое изменения текста
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String): Boolean {
                val result =
                    getSearchResultUseCase.execute(newText, getFavoritesFilmsUseCase.execute())
                filmsAdapter.changeItems(result)
                filmsAdapter.notifyDataSetChanged()
                return true
            }
        })
    }
}