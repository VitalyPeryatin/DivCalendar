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

class CalendarViewModel : ViewModel() {

    private val paymentInteractor = PaymentInteractor()
    private val rateInteractor = RateInteractor()
    private val settingsInteractor = SettingsInteractor()
    private val portfolioInteractor = PortfolioInteractor()

    private var paymentsFileCreator: PaymentsFileCreator? = null

    private val _state = MutableLiveData<Int>()
    val state: LiveData<Int>
        get() = _state

    private val _payments = MutableLiveData<List<IComparableItem>>()
    val payments: LiveData<List<IComparableItem>>
        get() = _payments

    private val _currentYear = MutableLiveData(paymentInteractor.getSelectedYear())
    val currentYear: LiveData<String>
        get() = _currentYear

    private var cachedPayments: List<MonthlyPayment> = emptyList()
    private val paymentsMapper = PaymentsToPresentationModelMapper()

    private val _isIncludeTaxes = MutableLiveData<Boolean?>(null)
    val isIncludeTaxes: LiveData<Boolean?>
        get() = _isIncludeTaxes

    private var _isHideCopecks = settingsInteractor.isHideCopecks()

    val sendFileEvent = LiveEvent<File?>()
    val portfolioNameTitleEvent = LiveEvent<String>()
    val showLoadingDialogEvent = LiveEvent<Boolean>()

    var paymentsJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadAllPayments(context: Context) = viewModelScope.launch {

        loadPortfolioName()

        if (portfolioInteractor.isCurrentPortfolioEmpty()) {
            _state.value = VIEW_STATE_CALENDAR_EMPTY
            return@launch
        }

        paymentsJob?.cancel()
        paymentsJob = paymentInteractor.getPayments()
            .onEach { cachedPayments = it }
            .map { paymentsMapper.mapToPresentationModel(context, cachedPayments) }
            .flowOn(Dispatchers.IO)
            .onStart {
                if (_state.value != VIEW_STATE_CALENDAR_CONTENT)
                    _state.value = VIEW_STATE_CALENDAR_LOADING
            }
            .onEach {
                _payments.value = it
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

    private fun loadPortfolioName() {
        val portfolioName = portfolioInteractor.getCurrentPortfolioName()
        portfolioNameTitleEvent.value = portfolioName
    }

    fun getFooterPositionByMonthNumber(monthNumber: Int): Int {
        return _payments.value!!.indexOfFirst {
            it is FooterPaymentPresentationModel && it.id == monthNumber
        }
    }

    fun exportData(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        showLoadingDialogEvent.postValue(true)
        paymentsFileCreator = ExcelPaymentsFileCreator(context)
        val exportFilePath = paymentsFileCreator?.create() ?: return@launch
        showLoadingDialogEvent.postValue(false)
        sendFileEvent.postValue(exportFilePath)
    }

    fun setDisplayCurrency(context: Context, currency: String) = viewModelScope.launch {
        rateInteractor.saveDisplayCurrency(currency)
        val payments = paymentsMapper.mapToPresentationModel(context, cachedPayments)
        _payments.postValue(payments)
    }

    fun selectYear(context: Context, selectedYear: String) {
        if (_currentYear.value == null || selectedYear != _currentYear.value) {
            paymentInteractor.setSelectedYear(selectedYear)
            _currentYear.value = selectedYear
            loadAllPayments(context)
        }
    }

    fun getDisplayCurrency(): String {
        return rateInteractor.getDisplayCurrency()
    }

    fun updatePastPayment(context: Context, editPaymentParams: EditPaymentParams) = viewModelScope.launch {
        paymentInteractor.updatePastPayment(editPaymentParams)
        loadAllPayments(context)
    }

    fun updateData(context: Context) {
        val newIsIncludedTaxes = settingsInteractor.isIncludeTaxes()

        val newIsHideCopecks = settingsInteractor.isHideCopecks()

        val hasNewData = (newIsIncludedTaxes != isIncludeTaxes.value || newIsHideCopecks != _isHideCopecks)

        _isIncludeTaxes.value = newIsIncludedTaxes

        _isHideCopecks = newIsHideCopecks

        if (hasNewData) {
            loadAllPayments(context)
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