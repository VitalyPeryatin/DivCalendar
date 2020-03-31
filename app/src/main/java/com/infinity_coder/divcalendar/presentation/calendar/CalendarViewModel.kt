package com.infinity_coder.divcalendar.presentation.calendar

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel
import com.infinity_coder.divcalendar.data.repositories.PaymentRepository
import com.infinity_coder.divcalendar.presentation.models.ChartPresentationModel
import com.infinity_coder.divcalendar.presentation.models.FooterPaymentPresentationModel
import com.infinity_coder.divcalendar.presentation.models.HeaderPaymentPresentationModel
import com.infinity_coder.divcalendar.presentation.models.PaymentPresentationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

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

    fun getPositionMonth(numberMonth: Int): Int {
        return _payments.value!!.indexOfFirst {
            it is FooterPaymentPresentationModel && it.id == numberMonth
        }
    }

    private fun loadAllPayments() = viewModelScope.launch {
        _state.postValue(VIEW_STATE_CALENDAR_LOADING)
        // TODO: Удалить задержку, когда будем получать реальные данные
        val payments = withContext(Dispatchers.IO) {
            mapPaymentsToPresentationModels(PaymentRepository.loadAllPayments())
        }
        _payments.postValue(payments)
        _state.postValue(VIEW_STATE_CALENDAR_CONTENT)
    }

    private suspend fun mapPaymentsToPresentationModels(payments: List<PaymentNetworkModel>): List<IComparableItem> {
        val items = mutableListOf<IComparableItem>()
        items.add(mapPaymentsToChartPresentationModel(payments))
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

    private suspend fun mapPaymentsToChartPresentationModel(payments: List<PaymentNetworkModel>): ChartPresentationModel {
        val annualIncome = payments.sumByDouble { it.dividends }
        val groupPayments = payments.groupBy { it.date.split("-")[1] }
            .toList()
            .sortedBy {
                it.first
            }
            .map {
                Pair(it.first.toInt(), it.second)
            }
        val color = mutableListOf<Int>().apply {
            groupPayments.forEach {
                it.second.forEach { payment ->
                    this.add(getColor(payment.logo))
                }
            }
        }
        return ChartPresentationModel(
            annualIncome.toString(),
            groupPayments,
            color
        )
    }

    private fun getColor(url: String): Int {
        return try {
            val inputStream: InputStream = URL(url).openStream()
            val image = BitmapFactory.decodeStream(inputStream)
            Palette.from(image).generate().getDominantColor(0)
        } catch (e: Exception) {
            Color.BLACK
        }
    }

    companion object {
        const val VIEW_STATE_CALENDAR_LOADING = 1
        const val VIEW_STATE_CALENDAR_CONTENT = 2
        const val VIEW_STATE_CALENDAR_EMPTY = 3
        const val VIEW_STATE_CALENDAR_NO_NETWORK = 4
    }
}