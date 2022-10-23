package com.andreikslpv.filmfinder.presentation.fragments

import android.os.Bundle
import android.transition.Scene
import android.transition.Slide
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.datasource.models.FilmsLocalModel
import com.andreikslpv.filmfinder.domain.Pages
import com.andreikslpv.filmfinder.presentation.MainActivity
import com.andreikslpv.filmfinder.presentation.recyclers.FilmListRecyclerAdapter
import com.andreikslpv.filmfinder.presentation.recyclers.itemDecoration.TopSpacingItemDecoration
import com.andreikslpv.filmfinder.presentation.recyclers.touchHelper.FilmTouchHelperCallback
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentAnimation()
        initFilmListRecycler()
        initSearchView()
    }

    private fun setFragmentAnimation() {
        val root = requireView().findViewById<ConstraintLayout>(R.id.home_fragment_root)
        val scene =
            Scene.getSceneForLayout(root, R.layout.merge_home_screen_content, requireContext())
        //Создаем анимацию выезда поля поиска сверху
        val searchSlide = Slide(Gravity.TOP).addTarget(R.id.home_app_bar)
        //Создаем анимацию выезда RV снизу
        val recyclerSlide = Slide(Gravity.BOTTOM).addTarget(R.id.film_list_recycler)
        //Создаем экземпляр TransitionSet, который объединит все наши анимации
        val customTransition = TransitionSet().apply {
            //Устанавливаем время, за которое будет проходить анимация
            duration = 500
            //Добавляем сами анимации
            addTransition(recyclerSlide)
            addTransition(searchSlide)
        }
        // запускаем через TransitionManager, вторым параметром передаем нашу кастомную анимацию
        TransitionManager.go(scene, customTransition)
    }

    private fun initFilmListRecycler() {
        val filmListRecycler = requireView().findViewById<RecyclerView>(R.id.film_list_recycler)
        filmListRecycler.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: FilmsLocalModel) {
                        (requireActivity() as MainActivity).launchDetailsFragment(
                            (activity as MainActivity).filmsRepository.getFilmLocalState(film)
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
        filmsAdapter.changeItems(getCurrentFilmsList())
    }

    private fun getCurrentFilmsList(): List<FilmsLocalModel> {
        return when ((activity as MainActivity).currentPage) {
            Pages.HOME -> {
                (activity as MainActivity).filmsRepository.getAllFilms()
            }
            Pages.FAVORITES -> {
                (activity as MainActivity).filmsRepository.getFavoriteFilms()
            }
            Pages.WATCH_LATER -> {
                (activity as MainActivity).filmsRepository.getWatchLaterFilms()
            }
        }
    }

    private fun initSearchView() {
        val searchView = requireView().findViewById<SearchView>(R.id.search_view)
        searchView.setOnClickListener {
            searchView.isIconified = false
        }
        //Подключаем слушателя изменений введенного текста в поиска
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            //Этот метод отрабатывает на каждое изменения текста
            override fun onQueryTextChange(newText: String): Boolean {
                //Если ввод пуст то вставляем в адаптер всю БД
                if (newText.isEmpty()) {
                    filmsAdapter.changeItems(getCurrentFilmsList())
                    return true
                }
                //Фильтруем список на поиск подходящих сочетаний
                val result = getCurrentFilmsList().filter {
                    //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                    it.title.lowercase(Locale.getDefault())
                        .contains(newText.lowercase(Locale.getDefault()))
                }
                //Добавляем в адаптер
                filmsAdapter.changeItems(result)
                return true
            }
        })
    }

}