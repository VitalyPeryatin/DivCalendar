package com.infinity_coder.divcalendar.presentation.calendar

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.domain.PaymentInteractor
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import com.infinity_coder.divcalendar.domain.RateInteractor
import com.infinity_coder.divcalendar.domain.SettingsInteractor
import com.infinity_coder.divcalendar.domain.models.EditPaymentParams
import com.infinity_coder.divcalendar.presentation._common.LiveEvent
import com.infinity_coder.divcalendar.presentation._common.logException
import com.infinity_coder.divcalendar.presentation.calendar.mappers.PaymentsToPresentationModelMapper
import com.infinity_coder.divcalendar.presentation.calendar.models.FooterPaymentPresentationModel
import com.infinity_coder.divcalendar.presentation.calendar.models.MonthlyPayment
import com.infinity_coder.divcalendar.presentation.export_sheet.PaymentsFileCreator
import com.infinity_coder.divcalendar.presentation.export_sheet.excel.ExcelPaymentsFileCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.util.*

class CalendarViewModel : ViewModel() {

    private val paymentInteractor = PaymentInteractor()
    private val rateInteractor = RateInteractor()
    private val settingsInteractor = SettingsInteractor()
    private val portfolioInteractor = PortfolioInteractor()

    private val _state = MutableLiveData<Int>()
    val state: LiveData<Int>
        get() = _state

    private val _payments = MutableLiveData<List<IComparableItem>>()
    val payments: LiveData<List<IComparableItem>>
        get() = _payments

    private val _currentYear = MutableLiveData(paymentInteractor.getSelectedYear())
    val currentYear: LiveData<String>
        get() = _currentYear

    val sendFileEvent = LiveEvent<File?>()
    val scrollingCalendarEvent = LiveEvent<Int>()
    val portfolioNameTitleEvent = LiveEvent<String>()
    val isIncludeTaxesEvent = LiveEvent<Boolean>()
    val showLoadingDialogEvent = LiveEvent<Boolean>()

    private val paymentsMapper = PaymentsToPresentationModelMapper()
    private var paymentsFileCreator: PaymentsFileCreator? = null
    private var cachedPayments: List<MonthlyPayment> = emptyList()
    private var paymentsJob: Job? = null

    init {
        loadAllPayments()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadAllPayments() = viewModelScope.launch {

        portfolioNameTitleEvent.value = portfolioInteractor.getCurrentPortfolioName()
        isIncludeTaxesEvent.value = settingsInteractor.isIncludeTaxes()

        if (portfolioInteractor.isCurrentPortfolioEmpty()) {
            _state.value = VIEW_STATE_CALENDAR_EMPTY
            return@launch
        }

        paymentsJob?.cancel()
        paymentsJob = paymentInteractor.getPayments()
            .onEach { cachedPayments = it }
            .map { paymentsMapper.mapToPresentationModel(cachedPayments) }
            .flowOn(Dispatchers.IO)
            .onStart {
                if (_state.value != VIEW_STATE_CALENDAR_CONTENT) {
                    _state.value = VIEW_STATE_CALENDAR_LOADING
                } else {
                    scrollingCalendarEvent.value = getFooterPositionByCurrentMonth()
                }
            }
            .onEach {
                _payments.value = it
                if (_state.value == VIEW_STATE_CALENDAR_LOADING) {
                    scrollingCalendarEvent.value = getFooterPositionByCurrentMonth()
                }
                if (it.isNotEmpty()) {
                    _state.value = VIEW_STATE_CALENDAR_CONTENT
                }
            }
            .onCompletion {
                if (_payments.value!!.isEmpty()) {
                    _state.value = VIEW_STATE_CALENDAR_EMPTY
                }
            }
            .catch { handleError(it) }
            .launchIn(viewModelScope)
    }

    private fun handleError(error: Throwable) {
        logException(this, error)
        if (error is HttpException) {
            _state.value = VIEW_STATE_CALENDAR_EMPTY_SECURITIES
        } else {
            _state.value = VIEW_STATE_CALENDAR_NO_NETWORK
        }
    }

    private fun getFooterPositionByCurrentMonth(): Int {
        return if (settingsInteractor.isScrollingCalendarForCurrentMonth()) {
            val monthNumber = Calendar.getInstance().get(Calendar.MONTH)
            getFooterPositionByMonthNumber(monthNumber)
        } else {
            0
        }
    }

    fun getFooterPositionByMonthNumber(monthNumber: Int): Int {
        val position = _payments.value!!.indexOfFirst {
            it is FooterPaymentPresentationModel && it.id == monthNumber
        }
        return if (position == -1) 0 else position
    }

    fun exportData(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        showLoadingDialogEvent.postValue(true)
        paymentsFileCreator = ExcelPaymentsFileCreator(context)
        val exportFilePath = paymentsFileCreator?.create() ?: return@launch
        showLoadingDialogEvent.postValue(false)
        sendFileEvent.postValue(exportFilePath)
    }

    fun setDisplayCurrency(currency: String) = viewModelScope.launch {
        rateInteractor.saveDisplayCurrency(currency)
        val payments = paymentsMapper.mapToPresentationModel(cachedPayments)
        _payments.postValue(payments)
    }

    fun selectYear(selectedYear: String) {
        if (_currentYear.value == null || selectedYear != _currentYear.value) {
            paymentInteractor.setSelectedYear(selectedYear)
            _currentYear.value = selectedYear
            loadAllPayments()
        }
    }

    fun getDisplayCurrency(): String {
        return rateInteractor.getDisplayCurrency()
    }

    fun updatePastPayment(editPaymentParams: EditPaymentParams) = viewModelScope.launch {
        paymentInteractor.updatePastPayment(editPaymentParams)
        loadAllPayments()
    }

    companion object {
        const val VIEW_STATE_CALENDAR_LOADING = 1
        const val VIEW_STATE_CALENDAR_CONTENT = 2
        const val VIEW_STATE_CALENDAR_EMPTY = 3
        const val VIEW_STATE_CALENDAR_NO_NETWORK = 4
        const val VIEW_STATE_CALENDAR_EMPTY_SECURITIES = 5
    }
}