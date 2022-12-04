package com.andreikslpv.filmfinder.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andreikslpv.filmfinder.databinding.FragmentSelectionsBinding
import com.andreikslpv.filmfinder.presentation.ui.utils.AnimationHelper
import com.andreikslpv.filmfinder.presentation.ui.MainActivity

class SelectionsFragment : Fragment() {
    private var _binding: FragmentSelectionsBinding? = null
    private val binding
        get() = _binding!!

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