package com.andreikslpv.filmfinder.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.presentation.AnimationHelper
import com.andreikslpv.filmfinder.presentation.MainActivity

class SelectionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_selections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AnimationHelper.performFragmentCircularRevealAnimation(requireView(), requireActivity(), 4)
    }

    override fun onPause() {
        super.onPause()

        // меняем background MainActivity на background фрагмента
        val layout =
            requireView().findViewById<FrameLayout>(R.id.selections_fragment_root)
        (activity as MainActivity).setBackground(layout.background)
    }
}