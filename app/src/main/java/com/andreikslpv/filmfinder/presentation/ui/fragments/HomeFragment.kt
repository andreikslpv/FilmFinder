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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.FragmentHomeBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.domain.usecase.apicache.GetPagedSearchResultUseCase
import com.andreikslpv.filmfinder.domain.usecase.management.ChangeApiAvailabilityUseCase
import com.andreikslpv.filmfinder.presentation.ui.MainActivity
import com.andreikslpv.filmfinder.presentation.ui.customviews.RatingDonutView
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmLoadStateAdapter
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmOnItemClickListener
import com.andreikslpv.filmfinder.presentation.ui.recyclers.FilmPagingAdapter
import com.andreikslpv.filmfinder.presentation.ui.utils.*
import com.andreikslpv.filmfinder.presentation.vm.HomeFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    private val autoDisposable = AutoDisposable()

    private lateinit var adapter: FilmPagingAdapter
    private val viewModel: HomeFragmentViewModel by viewModels()

    @Inject
    lateinit var changeApiAvailabilityUseCase: ChangeApiAvailabilityUseCase

    @Inject
    lateinit var getPagedSearchResultUseCase: GetPagedSearchResultUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.dagger.inject(this)
        // привязываемся к ЖЦ компонента
        autoDisposable.bindTo(lifecycle)
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

        setCollectors()

        setupSwipeToRefresh()
        initSettingsButton()
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

    private fun setCollectors() {
        this.lifecycleScope.launch {
            // Suspend the coroutine until the lifecycle is DESTROYED.
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                // Safely collect from source when the lifecycle is STARTED
                // and stop collecting when the lifecycle is STOPPED

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.currentApiFlow
                        .collect {
                            when (it) {
                                ValuesType.TMDB -> binding.homeToolbar.setNavigationIcon(R.drawable.ic_logo_tmdb)
                                ValuesType.IMDB -> binding.homeToolbar.setNavigationIcon(R.drawable.ic_logo_imdb)
                                else -> {}
                            }
                            if (viewModel.isNewApi(it)) {
                                adapter.refresh()
                            }
                        }
                }
            }
            // Note: at this point, the lifecycle is DESTROYED!
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
                    film,
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

    private fun initLoadStateListening() {
        this.lifecycleScope.launch {
            adapter.loadStateFlow.collect {
                if (it.source.prepend is LoadState.NotLoading) {
                    binding.homeProgressBar.visible(true)
                }
                if (it.source.prepend is LoadState.Error) {
                    catchError((it.source.prepend as LoadState.Error).error.message ?: "")
                }
                if (it.source.append is LoadState.Error) {
                    catchError((it.source.append as LoadState.Error).error.message ?: "")
                }
                if (it.source.refresh is LoadState.NotLoading) {
                    binding.homeProgressBar.visible(false)
                }
                if (it.source.refresh is LoadState.Error) {
                    binding.homeProgressBar.visible(false)
                    catchError((it.source.refresh as LoadState.Error).error.message ?: "")
                }
            }
        }
    }

    private fun catchError(message: String) {
        if (!binding.homeSearchView.query.isNullOrBlank()) {
            "${getString(R.string.error_failed_download)} $message"
                .makeToast(requireContext())
            changeApiAvailabilityUseCase.execute(false)
            adapter.refresh()
            (activity as MainActivity).updateMessageBoard(getString(R.string.error_search_from_cache))
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

    private fun initSearchView() {
        binding.homeSearchView.setOnClickListener {
            binding.homeSearchView.isIconified = false
        }

        Observable.create { subscriber ->
            //Вешаем слушатель на клавиатуру
            binding.homeSearchView.setOnQueryTextListener(object :
            //Вызывается на ввод символов
                SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isNotBlank()) {
                        setRecyclerVisibility(true)
                        subscriber.onNext(newText)
                    } else {
                        setRecyclerVisibility(false)
                    }
                    return false
                }

                //Вызывается по нажатию кнопки "Поиск"
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isNotBlank()) {
                        setRecyclerVisibility(true)
                        subscriber.onNext(query)
                    } else {
                        setRecyclerVisibility(false)
                    }
                    return false
                }
            })
        }
            .subscribeOn(Schedulers.io())
            .map {
                it.lowercase(Locale.getDefault()).trim()
            }
            .debounce(500, TimeUnit.MILLISECONDS)
            .flatMap {
                getPagedSearchResultUseCase.execute(it).toObservable()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    println("I/o onError")
                },
                onNext = {
                    adapter.submitData(lifecycle, it)
                }
            )
            .addTo(autoDisposable)
    }

    private fun setRecyclerVisibility(visible: Boolean) {
        binding.homeRecycler.isInvisible = !visible
        binding.loadStateView.isInvisible = !binding.homeRecycler.isInvisible
    }

    private fun setupSwipeToRefresh() {
        binding.homeSwipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
            binding.homeSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initSettingsButton() {
        binding.homeToolbar.menu.findItem(R.id.settingsButton).setOnMenuItemClickListener {
            (requireActivity() as MainActivity).launchSettingsFragment()
            true
        }
    }

}