package com.andreikslpv.filmfinder.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.FragmentSelectionsBinding
import com.andreikslpv.filmfinder.domain.CategoryType
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.usecase.GetAvailableCategories
import com.andreikslpv.filmfinder.domain.usecase.GetFilmLocalStateUseCase
import com.andreikslpv.filmfinder.presentation.ui.MainActivity
import com.andreikslpv.filmfinder.presentation.ui.customviews.RatingDonutView
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmLoadStateAdapter
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmOnItemClickListener
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmPagingAdapter
import com.andreikslpv.filmfinder.presentation.ui.utils.AnimationHelper
import com.andreikslpv.filmfinder.presentation.ui.utils.simpleScan
import com.andreikslpv.filmfinder.presentation.vm.SelectionsFragmentViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectionsFragment : Fragment() {
    private var _binding: FragmentSelectionsBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var getFilmLocalStateUseCase: GetFilmLocalStateUseCase

    @Inject
    lateinit var getAvailableCategories: GetAvailableCategories
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SelectionsFragmentViewModel::class.java)
    }
    private lateinit var spinnerList: MutableList<String>
    private lateinit var categoryList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.dagger.inject(this)
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
    }

    private fun initSpinner() {
        val categoryMap = getAvailableCategories.execute()
        mapToLists(categoryMap)

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner, spinnerList
        )
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        binding.selectionsSpinner.adapter = spinnerAdapter

        binding.selectionsSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 >= 0 && p2 < categoryList.size) {
                    viewModel.setCategory(categoryList[p2])
                } else {
                    Toast.makeText(requireContext(), R.string.error_category, Toast.LENGTH_SHORT)
                        .show()
                    viewModel.setCategory("")
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun mapToLists(map: Map<CategoryType, String>) {
        spinnerList = emptyList<String>().toMutableList()
        categoryList = emptyList<String>().toMutableList()
        for (entity in map) {
            when (entity.key) {
                CategoryType.POPULAR -> {
                    spinnerList.add(getString(R.string.category_popular))
                    categoryList.add(entity.value)
                }
                CategoryType.TOP_RATED -> {
                    spinnerList.add(getString(R.string.category_top_rated))
                    categoryList.add(entity.value)
                }
                CategoryType.NOW_PLAYING -> {
                    spinnerList.add(getString(R.string.category_now_playing))
                    categoryList.add(entity.value)
                }
                CategoryType.UPCOMING -> {
                    spinnerList.add(getString(R.string.category_upcoming))
                    categoryList.add(entity.value)
                }
            }
        }
    }

    private fun initFilmListRecycler() {
        val adapter = FilmPagingAdapter(object : FilmOnItemClickListener {
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
        binding.selectionsRecycler.layoutManager = LinearLayoutManager(requireContext())
        //binding.selectionsRecycler.setHasFixedSize(true)

        binding.selectionsRecycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = FilmLoadStateAdapter { adapter.retry() },
            footer = FilmLoadStateAdapter { adapter.retry() }
        )

        observeFilms(adapter)
        handleScrollingToTopWhenSearching(adapter)
    }

    private fun observeFilms(adapter: FilmPagingAdapter) {
        this.lifecycleScope.launch {
            viewModel.filmsFlow.collectLatest { pagedData ->
                adapter.submitData(pagedData)
            }
        }
    }

    // Когда пользователь меняет поисковой запрос, то отслеживаем этот момент и прокручиваем в начало списка
    private fun handleScrollingToTopWhenSearching(adapter: FilmPagingAdapter) =
        this.lifecycleScope.launch {
            getRefreshLoadStateFlow(adapter)
                .simpleScan(count = 2)
                .collectLatest { (previousState, currentState) ->
                    if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                        binding.selectionsRecycler.scrollToPosition(0)
                    }
                }
        }

    private fun getRefreshLoadStateFlow(adapter: FilmPagingAdapter): Flow<LoadState> {
        return adapter.loadStateFlow
            .map { it.refresh }
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
}