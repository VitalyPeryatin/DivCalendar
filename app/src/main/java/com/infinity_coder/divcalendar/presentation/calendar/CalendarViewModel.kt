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
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import com.infinity_coder.divcalendar.domain.RateInteractor
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

    private var cachedPayments: List<PaymentNetworkModel> = emptyList()

    private val rateInteractor = RateInteractor()

    init {
        loadAllPayments()
    }

    fun getPositionMonth(numberMonth: Int): Int {
        return _payments.value!!.indexOfFirst {
            it is FooterPaymentPresentationModel && it.id == numberMonth
        }
    }

    private fun loadAllPayments() = viewModelScope.launch {
        _state.value = VIEW_STATE_CALENDAR_LOADING

        var payments: List<IComparableItem> = listOf()

        withContext(Dispatchers.IO) {
            cachedPayments = PaymentRepository.loadAllPayments()
            payments = mapPaymentsToPresentationModels(cachedPayments)
        }
        _payments.value = payments
        _state.value = VIEW_STATE_CALENDAR_CONTENT
    }

    private suspend fun mapPaymentsToPresentationModels(payments: List<PaymentNetworkModel>): List<IComparableItem> {
        val items = mutableListOf<IComparableItem>()
        val groupMonth = payments.groupBy { it.date.split("-")[1] }.toList()
        val currentCurrency = getDisplayCurrency()
        for (i in groupMonth.indices) {
            items.add(HeaderPaymentPresentationModel.from(groupMonth[i]))
            val paymentList = PaymentPresentationModel.from(groupMonth[i])
            paymentList.map {
                it.currentCurrency = currentCurrency
                it.dividends = convertCurrencies(it.dividends.toFloat(), it.originalCurrency, currentCurrency).toDouble()
            }
            items.addAll(paymentList)

            val totalPayments = FooterPaymentPresentationModel.from(groupMonth[i])
            totalPayments.currentCurrency = currentCurrency
            items.add(totalPayments)
            if (i != groupMonth.size - 1) {
                items.add(DividerPresentationModel)
            }
        }
        items.add(0, mapPaymentsToChartPresentationModel(payments))
        return items
    }

    private suspend fun convertCurrencies(value: Float, from: String, to: String): Float {
        return when {
            from == RateRepository.USD_RATE && to == RateRepository.RUB_RATE -> {
                value / rateInteractor.getUsdToRubRate()
            }
            from == RateRepository.RUB_RATE && to == RateRepository.USD_RATE -> {
                value / rateInteractor.getRubToUsdRate()
            }
            else -> value
        }
    }

    private fun mapPaymentsToChartPresentationModel(payments: List<PaymentNetworkModel>): ChartPresentationModel {
        val annualIncome = payments.sumByDouble { it.dividends }.toFloat()
        val annualYield = 0f
        val groupPayments = payments.groupBy { it.date.split("-")[1] }
            .toList()
            .sortedBy {
                it.first
            }.map {
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
            annualIncome, annualYield, getDisplayCurrency(),
            groupPayments, color
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

    fun setDisplayCurrency(currency: String) = viewModelScope.launch {
        rateInteractor.saveDisplayCurrency(currency)
        val payments = mapPaymentsToPresentationModels(cachedPayments)
        _payments.postValue(payments)
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