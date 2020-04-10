package com.infinity_coder.divcalendar.presentation._common

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.*
import com.infinity_coder.divcalendar.presentation.App
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@OptIn(ExperimentalCoroutinesApi::class)
object BillingDelegate {

    private lateinit var billingClient: BillingClient

    private val billingResultChannel = BroadcastChannel<BillingResult?>(Channel.CONFLATED)
    @OptIn(FlowPreview::class)
    private val billingResultFlow = billingResultChannel.asFlow()

    private val billingIsPurchasedChannel = BroadcastChannel<Boolean>(Channel.CONFLATED)
    @OptIn(FlowPreview::class)
    private val billingIsPurchasedFlow = billingIsPurchasedChannel.asFlow()

    private val billingParamsChannel = BroadcastChannel<BillingFlowParams>(Channel.CONFLATED)

    @OptIn(FlowPreview::class)
    private val billingParamsFlow = billingParamsChannel.asFlow()

    init {
        initInAppBilling()
    }

    private fun initInAppBilling() {
        billingClient = BillingClient.newBuilder(App.instance.applicationContext)
            .enablePendingPurchases()
            .setListener { responseResult, purchases ->
                Log.d("Billing", "init BillingListener")
                if (responseResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    for (purchase in purchases) {
                        Log.d("Billing", "send purchase: $purchase")
                        sendBillingPurchase(purchase)
                    }
                } else {
                    sendBillingResult(responseResult)
                }
            }.build()
    }

    fun startConnection(): Flow<BillingResult?>  {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                sendBillingResult(null)
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                sendBillingResult(billingResult)
            }
        })
        return billingResultFlow
    }

    fun subscribeOnCheckBilling(): Flow<Boolean> {
        return billingIsPurchasedFlow
    }

    private fun sendBillingResult(billingResult: BillingResult?) = GlobalScope.launch {
        billingResultChannel.send(billingResult)
    }

    private fun sendBillingPurchase(purchase: Purchase) = GlobalScope.launch {
        val isPurchased = checkPurchase(purchase)
        billingIsPurchasedChannel.send(isPurchased)
    }

    private fun sendBillingParams(billingParams: BillingFlowParams) = GlobalScope.launch {
        billingParamsChannel.send(billingParams)
    }

    private suspend fun checkPurchase(purchase: Purchase): Boolean {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                withContext(Dispatchers.IO) {
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
                }
            }
            return true
        }
        return false
    }

    suspend fun querySkuDetails(skuList: List<String>): Flow<BillingFlowParams> {
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
        val skuDetailsResult = withContext(Dispatchers.IO) {
            billingClient.querySkuDetails(params.build())
        }

        val result = skuDetailsResult.billingResult
        val skuDetailsList = skuDetailsResult.skuDetailsList

        if (result.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
            for (skuDetails in skuDetailsList) {
                val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetails)
                    .build()

                sendBillingParams(flowParams)
            }
        }
        return billingParamsFlow
    }

    fun launchBillingFlow(activity: Activity, flowParams: BillingFlowParams) {
        val billingResult = billingClient.launchBillingFlow(activity, flowParams)
        sendBillingResult(billingResult)
    }
}