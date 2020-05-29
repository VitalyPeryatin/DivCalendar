package com.infinity_coder.divcalendar.presentation.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnboardingViewModel : ViewModel() {

    private val _pagePosition = MutableLiveData<Int>()
    val pagePosition: LiveData<Int>
        get() = _pagePosition

    fun updatePagePosition(position: Int) {
        _pagePosition.value = position
    }
}