package com.andreikslpv.filmfinder.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.FragmentFavoritesBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.local.GetFilmLocalStateUseCase
import com.andreikslpv.filmfinder.presentation.ui.MainActivity
import com.andreikslpv.filmfinder.presentation.ui.customviews.RatingDonutView
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmOnItemClickListener
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmRecyclerAdapter
import com.andreikslpv.filmfinder.presentation.ui.recyclers.itemDecoration.TopSpacingItemDecoration
import com.andreikslpv.filmfinder.presentation.ui.recyclers.touchHelper.FilmTouchHelperCallback
import com.andreikslpv.filmfinder.presentation.ui.utils.AnimationHelper
import com.andreikslpv.filmfinder.presentation.vm.FavoritesFragmentViewModel
import javax.inject.Inject

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var getFilmLocalStateUseCase: GetFilmLocalStateUseCase
    private lateinit var filmsAdapter: FilmRecyclerAdapter
    private val viewModel: FavoritesFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.dagger.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AnimationHelper.performFragmentCircularRevealAnimation(requireView(), requireActivity(), 2)

        initFilmListRecycler()
        viewModel.filmsListLiveData.observe(viewLifecycleOwner) {
            filmsAdapter.changeItems(it)
            filmsAdapter.notifyDataSetChanged()
        }
//        initSearchView()
        initSettingsButton()
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
                FilmRecyclerAdapter(object : FilmOnItemClickListener {
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
            val callback = FilmTouchHelperCallback(adapter as FilmRecyclerAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(this)
        }
//        //Кладем нашу БД в RV
//        viewModel.getFavoritesFilms()
    }

//    private fun initSearchView() {
//        binding.favoritesSearchView.setOnClickListener {
//            binding.favoritesSearchView.isIconified = false
//        }
//
//        //Подключаем слушателя изменений введенного текста в поиска
//        binding.favoritesSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return true
//            }
//
//            //Этот метод отрабатывает на каждое изменения текста
//            override fun onQueryTextChange(newText: String): Boolean {
//                viewModel.getSearchResult(newText)
//                return true
//            }
//        })
//    }

    private fun initSettingsButton() {
        binding.favoritesToolbar.menu.findItem(R.id.settingsButton).setOnMenuItemClickListener {
            (requireActivity() as MainActivity).launchSettingsFragment()
            true
        }
    }
}