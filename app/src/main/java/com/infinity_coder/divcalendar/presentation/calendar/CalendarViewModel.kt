package com.infinity_coder.divcalendar.presentation.calendar

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.domain.CalendarInteractor
import com.infinity_coder.divcalendar.domain.models.PaymentsForMonth
import com.infinity_coder.divcalendar.presentation.calendar.models.*
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

    private val calendarInteractor = CalendarInteractor()

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
        val payments = withContext(Dispatchers.IO) {
            mapPaymentsToPresentationModels(calendarInteractor.getPayments())
        }
        _payments.postValue(payments)
        _state.postValue(VIEW_STATE_CALENDAR_CONTENT)
    }

    private suspend fun mapPaymentsToPresentationModels(monthlyPayments: List<PaymentsForMonth>): List<IComparableItem> {
        calendarInteractor.getPayments()
        val items = mutableListOf<IComparableItem>()
        items.add(mapPaymentsToChartPresentationModel(monthlyPayments))
        for (i in monthlyPayments.indices) {
            items.add(HeaderPaymentPresentationModel.from(monthlyPayments[i]))
            items.addAll(PaymentPresentationModel.from(monthlyPayments[i]))
            items.add(FooterPaymentPresentationModel.from(monthlyPayments[i]))
            if (i != monthlyPayments.size - 1) {
                items.add(DividerPresentationModel)
            }
        }
        return items
    }

    private suspend fun mapPaymentsToChartPresentationModel(monthlyPayments: List<PaymentsForMonth>): ChartPresentationModel {
        val annualIncome = monthlyPayments.sumByDouble { paymentsForMonth ->
            paymentsForMonth.payments.sumByDouble { it.dividends }
        }

        val allMonthlyPayments = (0..11).map { numberMonth ->
            val payments = monthlyPayments.find { it.month == numberMonth }?.payments ?: listOf()
            return@map PaymentsForMonth(numberMonth, payments)
        }

        val colors = allMonthlyPayments.map {
            return@map if (it.payments.isEmpty())
                listOf(Color.WHITE)
            else
                it.payments.map { payment -> getColor(payment.logo) }
        }.flatten()

        return ChartPresentationModel(
            annualIncome.toString(),
            allMonthlyPayments,
            colors
        )
    }

    private fun getColor(url: String): Int {
        // TODO сделать обработку SVG
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