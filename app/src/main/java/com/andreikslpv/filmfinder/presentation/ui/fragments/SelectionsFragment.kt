package com.andreikslpv.filmfinder.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.FragmentSelectionsBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.CategoryType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.presentation.ui.MainActivity
import com.andreikslpv.filmfinder.presentation.ui.customviews.RatingDonutView
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmLoadStateAdapter
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmOnItemClickListener
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmPagingAdapter
import com.andreikslpv.filmfinder.presentation.ui.utils.AnimationHelper
import com.andreikslpv.filmfinder.presentation.ui.utils.makeToast
import com.andreikslpv.filmfinder.presentation.ui.utils.simpleScan
import com.andreikslpv.filmfinder.presentation.ui.utils.visible
import com.andreikslpv.filmfinder.presentation.vm.SelectionsFragmentViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SelectionsFragment : Fragment() {
    private var _binding: FragmentSelectionsBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: SelectionsFragmentViewModel by viewModels()
    private lateinit var adapter: FilmPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.dagger.inject(this)

        this.lifecycleScope.launch {
            // Suspend the coroutine until the lifecycle is DESTROYED.
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                // Safely collect from source when the lifecycle is STARTED
                // and stop collecting when the lifecycle is STOPPED
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.filmsFlow
                        .collectLatest(adapter::submitData)
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.currentApiFlow
                        .collect {
                            when (it) {
                                ValuesType.TMDB -> binding.selectionsToolbar.setNavigationIcon(R.drawable.ic_logo_tmdb)
                                ValuesType.IMDB -> binding.selectionsToolbar.setNavigationIcon(R.drawable.ic_logo_imdb)
                                else -> {}
                            }
                            if (!viewModel.isOldApi(it)) {
                                viewModel.setCategoryList()
                                initSpinner()
                                adapter.refresh()
                            }
                        }
                }

//                viewLifecycleOwner.lifecycleScope.launch {
//                    viewModel.currentCategoryList
//                        .collect {
//                            initSpinner()
//                            adapter.refresh()
//                        }
//                }

            }
            // Note: at this point, the lifecycle is DESTROYED!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AnimationHelper.performFragmentCircularRevealAnimation(requireView(), requireActivity(), 4)

        initSpinner()
        initFilmListRecycler()
        setupSwipeToRefresh()
        initSettingsButton()
    }

    override fun onPause() {
        super.onPause()
        // меняем background MainActivity на background фрагмента
        (activity as MainActivity).setBackground(binding.selectionsFragmentRoot.background)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initSpinner() {
        // из списка категорий, доступных для текущего апи,
        // формируем список пунктов выпадающего меню
        val spinnerList = convertCategoryListToSpinnerList(viewModel.currentCategoryList.value)

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner, spinnerList
        )
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        binding.selectionsSpinner.adapter = spinnerAdapter

        binding.selectionsSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 >= 0 && p2 < viewModel.currentCategoryList.value.size) {
                    viewModel.setCategory(viewModel.currentCategoryList.value[p2])
                    println("I/o choose spinner item")
                } else {
                    Toast.makeText(requireContext(), R.string.error_category, Toast.LENGTH_SHORT)
                        .show()
                    viewModel.setCategory(CategoryType.NONE)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun convertCategoryListToSpinnerList(inputList: List<CategoryType>): List<String> {
        val resultList = emptyList<String>().toMutableList()
        for (entity in inputList) {
            when (entity) {
                CategoryType.POPULAR ->
                    resultList.add(getString(R.string.category_popular))
                CategoryType.TOP_RATED ->
                    resultList.add(getString(R.string.category_top_rated))
                CategoryType.NOW_PLAYING ->
                    resultList.add(getString(R.string.category_now_playing))
                CategoryType.UPCOMING ->
                    resultList.add(getString(R.string.category_upcoming))
                CategoryType.TOP_250 ->
                    resultList.add(getString(R.string.category_top_250))
                CategoryType.BOXOFFICE_WEEKEND ->
                    resultList.add(getString(R.string.category_boxoffice_weekend))
                CategoryType.BOXOFFICE_ALLTIME ->
                    resultList.add(getString(R.string.category_boxoffice_alltime))
                else -> {}
            }
        }
        return resultList
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
                    film,
                    image,
                    text,
                    rating
                )
            }
        })

        binding.selectionsRecycler.layoutManager = LinearLayoutManager(requireContext())
        //binding.selectionsRecycler.setHasFixedSize(true)

        binding.selectionsRecycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = FilmLoadStateAdapter { adapter.retry() },
            footer = FilmLoadStateAdapter { adapter.retry() }
        )

        initLoadStateListening()
        handleScrollingToTopWhenChangeCategory()
    }

    private fun initLoadStateListening() {
        this.lifecycleScope.launch {
            adapter.loadStateFlow.collect {
                if (it.source.prepend is LoadState.NotLoading) {
                    binding.selectionsProgressBar.visible(true)
                }
                if (it.source.prepend is LoadState.Error) {
                    (it.source.prepend as LoadState.Error).error.message?.makeToast(requireContext())
                }
                if (it.source.append is LoadState.Error) {
                    (it.source.append as LoadState.Error).error.message?.makeToast(requireContext())
                }
                if (it.source.refresh is LoadState.NotLoading) {
                    binding.selectionsProgressBar.visible(false)
                }
                if (it.source.refresh is LoadState.Error) {
                    binding.selectionsProgressBar.visible(false)
                    (it.source.refresh as LoadState.Error).error.message?.makeToast(requireContext())
                }
            }
        }
    }

    // Когда пользователь меняет категорию, то отслеживаем этот момент и прокручиваем в начало списка
    private fun handleScrollingToTopWhenChangeCategory() =
        this.lifecycleScope.launch {
            getRefreshLoadStateFlow()
                .simpleScan(count = 2)
                .collectLatest { (previousState, currentState) ->
                    if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                        binding.selectionsRecycler.scrollToPosition(0)
                    }
                }
        }

    private fun getRefreshLoadStateFlow(): Flow<LoadState> {
        return adapter.loadStateFlow.map { it.refresh }
    }

    private fun setupSwipeToRefresh() {
        binding.selectionsSwipeRefreshLayout.setOnRefreshListener {
            this.lifecycleScope.launch {
                adapter.refresh()
                binding.selectionsSwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun initSettingsButton() {
        binding.selectionsToolbar.menu.findItem(R.id.settingsButton).setOnMenuItemClickListener {
            (requireActivity() as MainActivity).launchSettingsFragment()
            true
        }
    }
}