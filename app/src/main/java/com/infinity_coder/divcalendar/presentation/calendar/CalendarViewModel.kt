package com.infinity_coder.divcalendar.presentation.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.repositories.PaymentRepository
import com.infinity_coder.divcalendar.presentation.mappers.PaymentMapperViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CalendarViewModel : ViewModel(){

    private val _state = MutableLiveData<State>()
    val state:LiveData<State>
        get() = _state

    var paymentMapperViewModel:PaymentMapperViewModel? = null

    init {
        loadAllPayments()
    }

    private fun loadAllPayments() = viewModelScope.launch {
        _state.postValue(State.Progress)
        delay(2000L)
        val payments = PaymentRepository.loadAllPayments()
        _state.postValue(State.Data(paymentMapperViewModel!!.mapPaymentsToViewModels(payments)))
    }
}