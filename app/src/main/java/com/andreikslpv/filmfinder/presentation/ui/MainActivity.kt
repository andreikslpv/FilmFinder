package com.andreikslpv.filmfinder.presentation.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.ActivityMainBinding
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.domain.types.SettingsType
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.domain.usecase.*
import com.andreikslpv.filmfinder.presentation.ui.customviews.RatingDonutView
import com.andreikslpv.filmfinder.presentation.ui.fragments.*
import com.andreikslpv.filmfinder.presentation.ui.utils.FragmentsType
import com.andreikslpv.filmfinder.presentation.ui.utils.visible
import com.andreikslpv.filmfinder.presentation.vm.MainActivityViewModel
import com.andreikslpv.filmfinder.presentation.vm.MainActivityViewModelFactory
import javax.inject.Inject


const val TIME_INTERVAL = 2000
const val TRANSITION_NAME_FOR_IMAGE = "image_name"
const val TRANSITION_NAME_FOR_TEXT = "text_name"
const val TRANSITION_NAME_FOR_RATING = "rating_name"
const val TRANSITION_DURATION = 800L
const val BUNDLE_KEY_FILM = "film"
const val BUNDLE_KEY_TYPE = "type"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0L

    private val detailsFragment = DetailsFragment()

    @Inject
    lateinit var viewModelFactory: MainActivityViewModelFactory
    private lateinit var viewModel: MainActivityViewModel

    @Inject
    lateinit var getAllSettingValueUseCase: GetAllSettingValueUseCase

    @Inject
    lateinit var getSettingValueUseCase: GetSettingValueUseCase

    @Inject
    lateinit var setApiDataSourceUseCase: SetApiDataSourceUseCase

    @Inject
    lateinit var setCacheModeUseCase: SetCacheModeUseCase

    @Inject
    lateinit var changeNetworkAvailabilityUseCase: ChangeNetworkAvailabilityUseCase

    init {
        App.instance.dagger.inject(this)
        // задание анимации для shared elements - imageview с постером и textview с описанием
        val detailsTransition = TransitionSet()
        detailsTransition.apply {
            addTransition(ChangeBounds())
            addTransition(ChangeTransform())
            addTransition(ChangeImageTransform())
            ordering = TransitionSet.ORDERING_TOGETHER
        }
        detailsFragment.sharedElementEnterTransition =
            detailsTransition.setDuration(TRANSITION_DURATION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]

        initApplicationSettings()
        observeCurrentFragment()
        initBottomNavigationMenu()
        observeNetworkAvailability()

        // если первый, то запускаем фрагмент Home
        if (savedInstanceState == null)
            changeFragment(HomeFragment(), FragmentsType.HOME)
    }

    private fun initApplicationSettings() {
        // устанавливаем сохраненные настройки приложения
        val settingsMap = getAllSettingValueUseCase.execute()
        for (entity in settingsMap)
            when (entity.key) {
                SettingsType.API_TYPE -> setApiDataSourceUseCase.execute(entity.value)
                SettingsType.CACHE_MODE -> setCacheModeUseCase.execute(entity.value)
                else -> {}
            }
        updateMessageBoard()
    }

    private fun initBottomNavigationMenu() {
        binding.bottomNavigation.setOnItemSelectedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentPlaceholder)
            when (it.itemId) {
                R.id.home -> {
                    // если активен не HomeFragment, то запускаем HomeFragment
                    if (currentFragment !is HomeFragment) {
                        val fragment = checkFragmentExistence(FragmentsType.HOME)
                        //В первом параметре, если фрагмент не найден и метод вернул null, то с помощью
                        //элвиса вызываем создание нового фрагмента
                        changeFragment(fragment ?: HomeFragment(), FragmentsType.HOME)
                    }
                    true
                }
                R.id.favorites -> {
                    if (currentFragment !is FavoritesFragment) {
                        val fragment = checkFragmentExistence(FragmentsType.FAVORITES)
                        changeFragment(fragment ?: FavoritesFragment(), FragmentsType.FAVORITES)
                    }
                    true
                }
                R.id.watch_later -> {
                    if (currentFragment !is WatchLaterFragment) {
                        val fragment = checkFragmentExistence(FragmentsType.WATCH_LATER)
                        changeFragment(fragment ?: WatchLaterFragment(), FragmentsType.WATCH_LATER)
                    }
                    true
                }
                R.id.selections -> {
                    if (currentFragment !is SelectionsFragment) {
                        val fragment = checkFragmentExistence(FragmentsType.SELECTIONS)
                        changeFragment(fragment ?: SelectionsFragment(), FragmentsType.SELECTIONS)
                    }
                    true
                }
                else -> false
            }
        }
    }

    //Ищем фрагмент по тегу, если он есть то возвращаем его, если нет, то null
    private fun checkFragmentExistence(type: FragmentsType): Fragment? =
        supportFragmentManager.findFragmentByTag(type.tag)

    private fun changeFragment(fragment: Fragment, type: FragmentsType) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentPlaceholder, fragment, type.tag)
            .addToBackStack(null)
            .commit()
        viewModel.setCurrentFragment(type)
    }

    fun launchDetailsFragment(
        film: FilmDomainModel,
        image: ImageView,
        text: TextView,
        rating: RatingDonutView
    ) {
        //Создаем "посылку"
        val bundle = Bundle()
        //Кладем переданный фильм в "посылку"
        bundle.putParcelable(BUNDLE_KEY_FILM, film)
        //Кладем тип фрагмента из которого происходит вызов в "посылку"
        bundle.putParcelable(BUNDLE_KEY_TYPE, viewModel.getCurrentFragment())
        //Прикрепляем "посылку" к фрагменту
        detailsFragment.arguments = bundle

        //Запускаем фрагмент Details
        supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(image, TRANSITION_NAME_FOR_IMAGE)
            .addSharedElement(text, TRANSITION_NAME_FOR_TEXT)
            .addSharedElement(rating, TRANSITION_NAME_FOR_RATING)
            .replace(R.id.fragmentPlaceholder, detailsFragment, "details")
            .addToBackStack(null)
            .commit()
        viewModel.setCurrentFragment(FragmentsType.DETAILS)
    }

    fun launchSettingsFragment() {
        val bundle = Bundle()
        //Кладем тип фрагмента из которого происходит вызов в "посылку"
        bundle.putParcelable(BUNDLE_KEY_TYPE, viewModel.getCurrentFragment())
        var fragment = checkFragmentExistence(FragmentsType.SETTINGS)
        if (fragment == null)
            fragment = SettingsFragment()
        fragment.arguments = bundle
        changeFragment(fragment, FragmentsType.SETTINGS)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // double tap for exit
        when (supportFragmentManager.findFragmentById(R.id.fragmentPlaceholder)) {
            is HomeFragment, is FavoritesFragment, is WatchLaterFragment, is SelectionsFragment -> {
                if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(this, R.string.main_backpress_message, Toast.LENGTH_SHORT).show()
                }
                backPressed = System.currentTimeMillis()
            }
            else -> {
                super.onBackPressed()
                viewModel.setCurrentFragmentFromPrevious()
            }
        }
    }

    private fun observeCurrentFragment() {
        viewModel.currentFragmentLiveData.observe(this) {
            setBottomNavigationIcon(it)
        }
    }

    private fun setBottomNavigationIcon(type: FragmentsType) {
        val home = binding.bottomNavigation.menu.findItem(R.id.home)
        val favorites = binding.bottomNavigation.menu.findItem(R.id.favorites)
        val watchLater = binding.bottomNavigation.menu.findItem(R.id.watch_later)
        val selections = binding.bottomNavigation.menu.findItem(R.id.selections)
        when (type) {
            FragmentsType.HOME -> {
                home.setIcon(R.drawable.ic_baseline_home)
                favorites.setIcon(R.drawable.ic_baseline_favorite_border)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later_border)
                selections.setIcon(R.drawable.ic_baseline_selections_border)
            }
            FragmentsType.FAVORITES -> {
                home.setIcon(R.drawable.ic_baseline_home_border)
                favorites.setIcon(R.drawable.ic_baseline_favorite)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later_border)
                selections.setIcon(R.drawable.ic_baseline_selections_border)
            }
            FragmentsType.WATCH_LATER -> {
                home.setIcon(R.drawable.ic_baseline_home_border)
                favorites.setIcon(R.drawable.ic_baseline_favorite_border)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later)
                selections.setIcon(R.drawable.ic_baseline_selections_border)
            }
            FragmentsType.SELECTIONS -> {
                home.setIcon(R.drawable.ic_baseline_home_border)
                favorites.setIcon(R.drawable.ic_baseline_favorite_border)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later_border)
                selections.setIcon(R.drawable.ic_baseline_selections)
            }
            else -> {
                home.setIcon(R.drawable.ic_baseline_home_border)
                favorites.setIcon(R.drawable.ic_baseline_favorite_border)
                watchLater.setIcon(R.drawable.ic_baseline_watch_later_border)
                selections.setIcon(R.drawable.ic_baseline_selections_border)
            }
        }
    }

    private fun observeNetworkAvailability() {
        viewModel.connectionLiveData.observe(this) {
            changeNetworkAvailabilityUseCase.execute(it)
            updateMessageBoard()
        }
    }

    fun updateMessageBoard(_cacheMode: ValuesType? = null) {
        val cacheMode = _cacheMode ?: getSettingValueUseCase.execute(SettingsType.CACHE_MODE)
        val isNetworkAvailable = viewModel.connectionLiveData.value
        isNetworkAvailable?.let {
            if (isNetworkAvailable) {
                if (cacheMode == ValuesType.ALWAYS) {
                    binding.networkStatus.visible(true)
                    binding.networkStatus.text = getString(R.string.main_message_cache)
                } else {
                    binding.networkStatus.visible(false)
                    binding.networkStatus.text = ""
                }
            } else {
                binding.networkStatus.visible(true)
                when (cacheMode) {
                    ValuesType.AUTO, ValuesType.ALWAYS ->
                        "${getString(R.string.main_message_offline)} ${getString(R.string.main_message_cache)}".also {
                            binding.networkStatus.text = it
                        }
                    ValuesType.NEVER -> binding.networkStatus.text =
                        getString(R.string.main_message_offline)
                    else -> {}
                }
            }
        }
    }

    fun setBackground(newBackground: Drawable?) {
        if (newBackground != null) {
            binding.mainLayout.background = newBackground
        }
    }
}