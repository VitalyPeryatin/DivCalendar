package com.infinity_coder.divcalendar.presentation.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel
import com.infinity_coder.divcalendar.data.repositories.PaymentRepository
import com.infinity_coder.divcalendar.presentation.models.FooterPaymentPresentationModel
import com.infinity_coder.divcalendar.presentation.models.HeaderPaymentPresentationModel
import com.infinity_coder.divcalendar.presentation.models.PaymentPresentationModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CalendarViewModel : ViewModel() {

    private val _state = MutableLiveData<Int>()
    val state: LiveData<Int>
        get() = _state

    private val _payments = MutableLiveData<List<IComparableItem>>()
    val payments: LiveData<List<IComparableItem>>
        get() = _payments

    init {
        loadAllPayments()
    }

    private fun loadAllPayments() = viewModelScope.launch {
        _state.postValue(VIEW_STATE_CALENDAR_LOADING)
        // TODO: Удалить задержку, когда будем получать реальные данные
        delay(2000L)
        val payments = PaymentRepository.loadAllPayments()
        _payments.postValue(mapPaymentsToPresentationModels(payments))
        _state.postValue(VIEW_STATE_CALENDAR_CONTENT)
    }

    private fun mapPaymentsToPresentationModels(payments: List<PaymentNetworkModel>): List<IComparableItem> {
        val items = mutableListOf<IComparableItem>()
        val groupMonth = payments.groupBy { it.date.split("-")[1] }.toList()
        for (i in groupMonth.indices) {
            items.add(HeaderPaymentPresentationModel.from(groupMonth[i]))
            items.addAll(PaymentPresentationModel.from(groupMonth[i]))
            items.add(FooterPaymentPresentationModel.from(groupMonth[i]))
            if (i != groupMonth.size - 1) {
                items.add(DividerItem)
            }
        }
        return items
    }

    companion object {
        const val VIEW_STATE_CALENDAR_LOADING = 1
        const val VIEW_STATE_CALENDAR_CONTENT = 2
        const val VIEW_STATE_CALENDAR_EMPTY = 3
        const val VIEW_STATE_CALENDAR_NO_NETWORK = 4
    }
}