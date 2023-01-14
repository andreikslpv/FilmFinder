package com.andreikslpv.filmfinder.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andreikslpv.filmfinder.App
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.FragmentSettingsBinding
import com.andreikslpv.filmfinder.domain.types.ValuesType
import com.andreikslpv.filmfinder.presentation.ui.BUNDLE_KEY_TYPE
import com.andreikslpv.filmfinder.presentation.ui.MainActivity
import com.andreikslpv.filmfinder.presentation.ui.utils.FragmentsType
import com.andreikslpv.filmfinder.presentation.vm.SettingsFragmentViewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var type: FragmentsType
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SettingsFragmentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.dagger.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        type = arguments?.get(BUNDLE_KEY_TYPE) as FragmentsType
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackground()
        initApiChips()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initApiChips() {
        viewModel.apiLiveData.observe(viewLifecycleOwner) {
            when (it) {
                ValuesType.TMDB -> {
                    binding.settingsApiChipGroup.check(R.id.settingsApiChipTmdb)
                }
                ValuesType.IMDB -> {
                    binding.settingsApiChipGroup.check(R.id.settingsApiChipImdb)
                }
                else -> {}
            }
        }
        binding.settingsApiChipGroup.setOnCheckedStateChangeListener { group, _ ->
            when (group.checkedChipId) {
                R.id.settingsApiChipTmdb -> viewModel.setApiType(ValuesType.TMDB)
                R.id.settingsApiChipImdb -> viewModel.setApiType(ValuesType.IMDB)
            }
        }
    }

    private fun setBackground() {
        // устанавливаем background в зависимости от типа фрагмента, из которого вызван фрагмент Settings
        when (type) {
            FragmentsType.HOME -> binding.settingsFragmentRoot.background =
                ResourcesCompat.getDrawable(resources, R.drawable.background_home, null)
            FragmentsType.FAVORITES -> binding.settingsFragmentRoot.background =
                ResourcesCompat.getDrawable(resources, R.drawable.background_favorites, null)
            FragmentsType.WATCH_LATER -> binding.settingsFragmentRoot.background =
                ResourcesCompat.getDrawable(resources, R.drawable.background_watch_later, null)
            FragmentsType.SELECTIONS -> binding.settingsFragmentRoot.background =
                ResourcesCompat.getDrawable(resources, R.drawable.background_selections, null)
            else -> binding.settingsFragmentRoot.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.background_details,
                    (activity as MainActivity).theme
                )
        }
    }

}