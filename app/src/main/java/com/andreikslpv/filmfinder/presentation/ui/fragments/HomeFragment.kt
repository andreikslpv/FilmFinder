package com.andreikslpv.filmfinder.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.FragmentHomeBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.domain.usecase.local.GetFilmLocalStateUseCase
import com.andreikslpv.filmfinder.presentation.ui.MainActivity
import com.andreikslpv.filmfinder.presentation.ui.customviews.RatingDonutView
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmLoadStateAdapter
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmOnItemClickListener
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmPagingAdapter
import com.andreikslpv.filmfinder.presentation.ui.utils.AnimationHelper
import com.andreikslpv.filmfinder.presentation.ui.utils.makeToast
import com.andreikslpv.filmfinder.presentation.ui.utils.simpleScan
import com.andreikslpv.filmfinder.presentation.ui.utils.visible
import com.andreikslpv.filmfinder.presentation.vm.HomeFragmentViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: FilmPagingAdapter

    @Inject
    lateinit var getFilmLocalStateUseCase: GetFilmLocalStateUseCase

    private val viewModel: HomeFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.dagger.inject(this)
    }

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

        initSearchView()
        initFilmListRecycler()
        observeFilmFlowStatus()
        setupSwipeToRefresh()
        observeApiType()
        initSettingsButton()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setApiType()
    }

    override fun onPause() {
        super.onPause()
        // меняем background MainActivity на background фрагмента
        (activity as MainActivity).setBackground(binding.homeFragmentRoot.background)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeFilmFlowStatus() {
        viewModel.filmsFlowInitStatus.observe(viewLifecycleOwner) {
            if (it)
                observeFilms()
        }
    }

    private fun initFilmListRecycler() {
        adapter = FilmPagingAdapter(object : FilmOnItemClickListener {
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
        binding.homeRecycler.layoutManager = LinearLayoutManager(requireContext())
        //binding.selectionsRecycler.setHasFixedSize(true)

        binding.homeRecycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = FilmLoadStateAdapter { adapter.retry() },
            footer = FilmLoadStateAdapter { adapter.retry() }
        )

        initLoadStateListening()
        handleScrollingToTopWhenSearching()
    }

    private fun observeFilms() {
        this.lifecycleScope.launch {
            viewModel.filmsFlow.collectLatest { pagedData ->
                adapter.submitData(pagedData)
            }
        }
    }

    private fun initLoadStateListening() {
        this.lifecycleScope.launch {
            adapter.loadStateFlow.collect {
                if (it.source.prepend is LoadState.NotLoading) {
                    binding.homeProgressBar.visible(true)
                }
                if (it.source.prepend is LoadState.Error) {
                    (it.source.prepend as LoadState.Error).error.message?.makeToast(requireContext())
                }
                if (it.source.append is LoadState.Error) {
                    (it.source.append as LoadState.Error).error.message?.makeToast(requireContext())
                }
                if (it.source.refresh is LoadState.NotLoading) {
                    binding.homeProgressBar.visible(false)
                }
                if (it.source.refresh is LoadState.Error) {
                    binding.homeProgressBar.visible(false)
                    (it.source.refresh as LoadState.Error).error.message?.makeToast(requireContext())
                }
            }
        }
    }

    // Когда пользователь меняет поисковой запрос, то отслеживаем этот момент и прокручиваем в начало списка
    private fun handleScrollingToTopWhenSearching() =
        this.lifecycleScope.launch {
            getRefreshLoadStateFlow()
                .simpleScan(count = 2)
                .collectLatest { (previousState, currentState) ->
                    if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                        binding.homeRecycler.scrollToPosition(0)
                    }
                }
        }

    private fun getRefreshLoadStateFlow(): Flow<LoadState> {
        return adapter.loadStateFlow
            .map { it.refresh }
    }

    private fun observeApiType() {
        viewModel.apiLiveData.observe(viewLifecycleOwner) {
            this.lifecycleScope.launch {
                refreshHomeFilmList()
            }
            when (it) {
                ValuesType.TMDB -> binding.homeToolbar.setNavigationIcon(R.drawable.ic_logo_tmdb)
                ValuesType.IMDB -> binding.homeToolbar.setNavigationIcon(R.drawable.ic_logo_imdb)
                else -> {}
            }
        }
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
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotBlank()) {
                    binding.homeRecycler.isInvisible = false
                    binding.loadStateView.isInvisible = !binding.homeRecycler.isInvisible
                    viewModel.setQuery(newText)
                    return true
                } else {
                    binding.homeRecycler.isInvisible = true
                    binding.loadStateView.isInvisible = !binding.homeRecycler.isInvisible
                }

                return true
            }
        })
    }

    private fun setupSwipeToRefresh() {
        binding.homeSwipeRefreshLayout.setOnRefreshListener {
            this.lifecycleScope.launch {
                refreshHomeFilmList()
                binding.homeSwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private suspend fun refreshHomeFilmList() {
        adapter.submitData(PagingData.empty())
        viewModel.refresh()
    }

    private fun initSettingsButton() {
        binding.homeToolbar.menu.findItem(R.id.settingsButton).setOnMenuItemClickListener {
            (requireActivity() as MainActivity).launchSettingsFragment()
            true
        }
    }

}