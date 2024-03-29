package com.andreikslpv.filmfinder.presentation.ui

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.view.View
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
import com.andreikslpv.filmfinder.domain.usecase.management.GetSettingValueUseCase
import com.andreikslpv.filmfinder.domain.usecase.management.InitApplicationSettingsUseCase
import com.andreikslpv.filmfinder.presentation.receivers.ChargeChecker
import com.andreikslpv.filmfinder.presentation.ui.customviews.RatingDonutView
import com.andreikslpv.filmfinder.presentation.ui.fragments.*
import com.andreikslpv.filmfinder.presentation.ui.utils.AutoDisposable
import com.andreikslpv.filmfinder.presentation.ui.utils.FragmentsType
import com.andreikslpv.filmfinder.presentation.ui.utils.addTo
import com.andreikslpv.filmfinder.presentation.ui.utils.makeToast
import com.andreikslpv.filmfinder.presentation.ui.utils.visible
import com.andreikslpv.filmfinder.presentation.vm.MainActivityViewModel
import com.andreikslpv.filmfinder.presentation.vm.MainActivityViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
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
    private val promoAnimDuration = 1500L
    private val promoAnimAlfa = 1f

    private lateinit var receiver: BroadcastReceiver

    private val detailsFragment = DetailsFragment()

    private val autoDisposable = AutoDisposable()

    @Inject
    lateinit var viewModelFactory: MainActivityViewModelFactory
    private lateinit var viewModel: MainActivityViewModel

    @Inject
    lateinit var initApplicationSettingsUseCase: InitApplicationSettingsUseCase

    @Inject
    lateinit var getSettingValueUseCase: GetSettingValueUseCase

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

        autoDisposable.bindTo(lifecycle)

        initApplicationSettings()
        initReceiver()
        observeCurrentFragment()
        initBottomNavigationMenu()
        observeMessage()

        // определяем какой запускать фрагмент
        if (savedInstanceState == null) {
            // запускаем в любом случае, для того чтобы создать бэкстэк
            changeFragment(HomeFragment(), FragmentsType.HOME)
            // пробуем получить из бандла фильм
            val film = if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra(BUNDLE_KEY_FILM, FilmDomainModel::class.java)
            } else {
                intent.getParcelableExtra(BUNDLE_KEY_FILM)
            }
            // если бандл содержит фильм, то запускаем фрагмент Details
            if (film != null)
                launchDetailsFragment(film, null, null, null)
        }

        observePromo()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun initApplicationSettings() {
        // устанавливаем сохраненные настройки приложения
        initApplicationSettingsUseCase.execute()
        updateMessageBoard()
    }

    private fun initReceiver() {
        receiver = ChargeChecker()
        // создаем фильтры для того, чтобы слушать нужные action
        val filters = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_BATTERY_LOW)
        }
        // регистрируем ресивер
        registerReceiver(receiver, filters)
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
        image: ImageView?,
        text: TextView?,
        rating: RatingDonutView?
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
        if (image != null && text != null && rating != null)
            supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .addSharedElement(image, TRANSITION_NAME_FOR_IMAGE)
                .addSharedElement(text, TRANSITION_NAME_FOR_TEXT)
                .addSharedElement(rating, TRANSITION_NAME_FOR_RATING)
                .replace(R.id.fragmentPlaceholder, detailsFragment, FragmentsType.DETAILS.tag)
                .addToBackStack(null)
                .commit()
        else
            supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentPlaceholder, detailsFragment, FragmentsType.DETAILS.tag)
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
            updateMessageBoard()
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

    private fun observeMessage() {
        viewModel.messageLiveData.observe(this) {
            if (it.isNullOrBlank()) {
                binding.messageBoard.visible(false)
            } else {
                binding.messageBoard.text = it
                binding.messageBoard.visible(true)
            }
        }
    }

    fun updateMessageBoard(_cacheMode: ValuesType? = null) {
        var result = ""
        val currentFragment = viewModel.currentFragmentLiveData.value
        val cacheMode = _cacheMode ?: getSettingValueUseCase.execute(SettingsType.CACHE_MODE)

        if (cacheMode == ValuesType.ALWAYS) {
            if (currentFragment == FragmentsType.HOME)
                result = getString(R.string.main_message_search_cache)
            if (currentFragment == FragmentsType.SELECTIONS)
                result = getString(R.string.main_message_cache)
        }

        viewModel.setMessage(result)
    }

    fun updateMessageBoard(message: String) {
        viewModel.setMessage(message)
    }

    fun setBackground(newBackground: Drawable?) {
        if (newBackground != null) {
            binding.mainLayout.background = newBackground
        }
    }


    private fun observePromo() {
        viewModel.promo
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.isNotEmpty())
                        enablePromo(it[0])
                },
                {
                    it.message?.makeToast(this)
                }
            )
            .addTo(autoDisposable)
    }

    private fun enablePromo(film: FilmDomainModel) {
        //Включаем промо верстку
        binding.promo.apply {
            //Делаем видимой
            visibility = View.VISIBLE
            //Анимируем появление
            animate()
                .setDuration(promoAnimDuration)
                .alpha(promoAnimAlfa)
                .start()
            //Вызываем метод, который загрузит постер в ImageView
            setLinkForPoster(film.posterDetails)
            //Кнопка, по нажатии на которую промо уберется
            closeButton.setOnClickListener {
                visibility = View.GONE
            }
            poster.setOnClickListener {
                launchDetailsFragment(film, null, null, null)
                visibility = View.GONE
            }
        }
    }
}