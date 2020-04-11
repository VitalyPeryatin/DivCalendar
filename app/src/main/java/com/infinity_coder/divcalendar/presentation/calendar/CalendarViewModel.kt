package com.infinity_coder.divcalendar.presentation.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.domain.CalendarInteractor
import com.infinity_coder.divcalendar.domain.RateInteractor
import com.infinity_coder.divcalendar.domain.models.MonthlyPayment
import com.infinity_coder.divcalendar.presentation.calendar.mappers.PaymentsToPresentationModelMapper
import com.infinity_coder.divcalendar.presentation.calendar.models.FooterPaymentPresentationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class CalendarViewModel : ViewModel() {

    private val _state = MutableLiveData<Int>()
    val state: LiveData<Int>
        get() = _state

    private val _payments = MutableLiveData<List<IComparableItem>>()
    val payments: LiveData<List<IComparableItem>>
        get() = _payments

    private var cachedPayments: List<MonthlyPayment> = emptyList()
    private var currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

    private val calendarInteractor = CalendarInteractor()
    private val rateInteractor = RateInteractor()

    private val paymentsMapper = PaymentsToPresentationModelMapper()

    init {
        loadAllPayments()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadAllPayments() = viewModelScope.launch {
        calendarInteractor.getPayments(currentYear)
            .onEach { cachedPayments = it }
            .map { paymentsMapper.mapToPresentationModel(cachedPayments) }
            .flowOn(Dispatchers.IO)
            .onStart { _state.value = VIEW_STATE_CALENDAR_LOADING }
            .onEach { _payments.value = it }
            .onCompletion { _state.value = VIEW_STATE_CALENDAR_CONTENT }
            .launchIn(viewModelScope)
    }

    fun getFooterPositionByMonthNumber(monthNumber: Int): Int {
        return _payments.value!!.indexOfFirst {
            it is FooterPaymentPresentationModel && it.id == monthNumber
        }
    }

    fun setDisplayCurrency(currency: String) = viewModelScope.launch {
        rateInteractor.saveDisplayCurrency(currency)
        val payments = paymentsMapper.mapToPresentationModel(cachedPayments)
        _payments.postValue(payments)
    }

    fun selectYear(selectedYear:String){
        if(selectedYear != currentYear) {
            currentYear = selectedYear
            loadAllPayments()
        }
    }

    fun getDisplayCurrency(): String {
        return rateInteractor.getDisplayCurrency()
    }

    companion object {
        const val VIEW_STATE_CALENDAR_LOADING = 1
        const val VIEW_STATE_CALENDAR_CONTENT = 2
        const val VIEW_STATE_CALENDAR_EMPTY = 3
        const val VIEW_STATE_CALENDAR_NO_NETWORK = 4
    }
}