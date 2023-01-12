package com.andreikslpv.filmfinder.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.databinding.FragmentDetailsBinding
import com.andreikslpv.filmfinder.databinding.FragmentSettingsBinding
import com.andreikslpv.filmfinder.presentation.ui.BUNDLE_KEY_TYPE
import com.andreikslpv.filmfinder.presentation.ui.MainActivity
import com.andreikslpv.filmfinder.presentation.ui.utils.FragmentsType

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var type: FragmentsType

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setBackground() {
        // устанавливаем background в зависимости от типа фрагмента, из которого вызван фрагмент Details
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