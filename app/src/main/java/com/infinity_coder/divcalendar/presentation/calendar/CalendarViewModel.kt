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

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    init {
        loadAllPayments()
    }

    private fun loadAllPayments() = viewModelScope.launch {
        _state.postValue(State.Progress)
        delay(2000L)
        val payments = PaymentRepository.loadAllPayments()
        _state.postValue(State.Data(mapPaymentsToPresentationModels(payments)))
    }

    private fun mapPaymentsToPresentationModels(payments: List<PaymentNetworkModel>):List<IComparableItem> {
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
}