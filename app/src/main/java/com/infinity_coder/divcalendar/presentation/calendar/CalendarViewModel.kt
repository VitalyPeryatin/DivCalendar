package com.infinity_coder.divcalendar.presentation.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.domain.CalendarInteractor
import com.infinity_coder.divcalendar.domain.RateInteractor
import com.infinity_coder.divcalendar.domain.SettingsInteractor
import com.infinity_coder.divcalendar.domain.models.MonthlyPayment
import com.infinity_coder.divcalendar.presentation._common.logException
import com.infinity_coder.divcalendar.presentation.calendar.mappers.PaymentsToPresentationModelMapper
import com.infinity_coder.divcalendar.presentation.calendar.models.FooterPaymentPresentationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CalendarViewModel : ViewModel() {

    private val _state = MutableLiveData<Int>()
    val state: LiveData<Int>
        get() = _state

    private val _payments = MutableLiveData<List<IComparableItem>>()
    val payments: LiveData<List<IComparableItem>>
        get() = _payments

    private val _currentYear = MutableLiveData<String>()
    val currentYear: LiveData<String>
        get() = _currentYear

    private var cachedPayments: List<MonthlyPayment> = emptyList()

    private val calendarInteractor = CalendarInteractor()
    private val rateInteractor = RateInteractor()
    private val settingsInteractor = SettingsInteractor()

    private val paymentsMapper = PaymentsToPresentationModelMapper()

    private val _isIncludeTaxes = MutableLiveData(false)
    val isIncludeTaxes: LiveData<Boolean>
        get() = _isIncludeTaxes

    init {
        _currentYear.value = calendarInteractor.getSelectedYear()
        loadAllPayments()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadAllPayments() = viewModelScope.launch {
        val currentYearValue = _currentYear.value!!
        val includeTaxes = isIncludeTaxes.value ?: false
        calendarInteractor.getPayments(currentYearValue, includeTaxes)
            .onEach { cachedPayments = it }
            .map { paymentsMapper.mapToPresentationModel(cachedPayments) }
            .flowOn(Dispatchers.IO)
            .onStart { _state.value = VIEW_STATE_CALENDAR_LOADING }
            .onEach {
                if (it.isEmpty()) {
                    _state.value = VIEW_STATE_CALENDAR_EMPTY
                } else {
                    _payments.value = it
                    _state.value = VIEW_STATE_CALENDAR_CONTENT
                }
            }
            .catch { handleError(it) }
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

    fun selectYear(selectedYear: String) {
        if (_currentYear.value == null || selectedYear != _currentYear.value) {
            calendarInteractor.setSelectedYear(selectedYear)
            _currentYear.value = selectedYear
            loadAllPayments()
        }
    }

    fun getDisplayCurrency(): String {
        return rateInteractor.getDisplayCurrency()
    }

    fun updateData() {

        val newIsIncludedTaxes = settingsInteractor.isIncludeTaxes()

        val hasNewData = newIsIncludedTaxes != isIncludeTaxes.value

        _isIncludeTaxes.value = newIsIncludedTaxes

        if (hasNewData) {
            loadAllPayments()
        }
    }

    private fun handleError(error: Throwable) {
        logException(this, error)
        if (error is HttpException) {
            _state.value = VIEW_STATE_CALENDAR_EMPTY_SECURITIES
        } else {
            _state.value = VIEW_STATE_CALENDAR_NO_NETWORK
        }
    }

    companion object {
        const val VIEW_STATE_CALENDAR_LOADING = 1
        const val VIEW_STATE_CALENDAR_CONTENT = 2
        const val VIEW_STATE_CALENDAR_EMPTY = 3
        const val VIEW_STATE_CALENDAR_NO_NETWORK = 4
        const val VIEW_STATE_CALENDAR_EMPTY_SECURITIES = 5
    }
}