package com.andreikslpv.filmfinder.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.FragmentSelectionsBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.presentation.ui.MainActivity
import com.andreikslpv.filmfinder.presentation.ui.customviews.RatingDonutView
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmLoadStateAdapter
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmOnItemClickListener
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmPagingAdapter
import com.andreikslpv.filmfinder.presentation.ui.utils.AnimationHelper
import com.andreikslpv.filmfinder.presentation.ui.utils.CategoryType
import com.andreikslpv.filmfinder.presentation.ui.utils.simpleScan
import com.andreikslpv.filmfinder.presentation.vm.SelectionsFragmentViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SelectionsFragment : Fragment() {
    private var _binding: FragmentSelectionsBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SelectionsFragmentViewModel::class.java)
    }
    private val categoryList = listOf(
        CategoryType.POPULAR,
        CategoryType.TOP_RATED,
        CategoryType.NOW_PLAYING,
        CategoryType.UPCOMING
    )
    private val spinnerList by lazy {
        categoryList.map { getString(it.res) }
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
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner, spinnerList
        )
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        binding.selectionsSpinner.adapter = spinnerAdapter

        binding.selectionsSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.setCategory(categoryList[p2].tmdbPath)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
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
                    App.instance.getFilmLocalStateUseCase.execute(film),
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