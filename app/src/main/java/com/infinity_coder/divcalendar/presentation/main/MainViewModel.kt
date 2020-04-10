package com.infinity_coder.divcalendar.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingFlowParams
import com.infinity_coder.divcalendar.presentation._common.BillingDelegate
import com.infinity_coder.divcalendar.presentation._common.LiveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel : ViewModel() {

    val billingParamsEvent = LiveEvent<BillingFlowParams>()

    private val _isPurchased = MutableLiveData<Boolean>()
    val isPurchased: LiveData<Boolean>
        get() = _isPurchased

    init {
        subscribeOnCheckBilling()
    }

    fun startConnection() {
        BillingDelegate.startConnection()
            .onEach { Log.d("Billing", it.toString()) }
            .launchIn(viewModelScope)
    }

    fun buySubscription() = viewModelScope.launch {
        BillingDelegate.querySkuDetails(listOf("standard_subscription"))
            .onEach { billingParamsEvent.value = it }
            .launchIn(viewModelScope)
    }

    private fun subscribeOnCheckBilling() {
        BillingDelegate.subscribeOnCheckBilling()
            .onEach { _isPurchased.value = it }
            .launchIn(viewModelScope)
    }
}