package com.infinity_coder.divcalendar.presentation.main

import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.domain.PaymentInteractor

class MainViewModel : ViewModel() {

    private val paymentInteractor = PaymentInteractor()

    override fun onCleared() {
        super.onCleared()
    }
}