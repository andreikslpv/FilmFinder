package com.andreikslpv.filmfinder.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.filmfinder.presentation.ui.utils.FragmentsType

class MainActivityViewModel : ViewModel(){
    val currentFragmentLiveData: MutableLiveData<FragmentsType> = MutableLiveData()
    private var previousFragmentsType = FragmentsType.NONE

    fun setCurrentFragment(newFragment: FragmentsType) {
        if (this.currentFragmentLiveData.value == newFragment) {
            return
        }
        else {
            previousFragmentsType = this.currentFragmentLiveData.value ?: FragmentsType.NONE
            this.currentFragmentLiveData.value = newFragment
        }
    }

    fun setCurrentFragmentFromPrevious() {
        this.currentFragmentLiveData.value = previousFragmentsType
    }

    fun getCurrentFragment() = currentFragmentLiveData.value
}