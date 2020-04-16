package com.infinity_coder.divcalendar.presentation._common

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.domain.SubscriptionInteractor
import com.infinity_coder.divcalendar.presentation.billing.BuySubscriptionActivity
import com.infinity_coder.divcalendar.presentation.billing.PremiumSubscriptionObservable
import com.infinity_coder.divcalendar.presentation.billing.PremiumSubscriptionObserver
import kotlinx.coroutines.*

abstract class AbstractSubscriptionActivity : AppCompatActivity(), BillingProcessor.IBillingHandler, PremiumSubscriptionObservable {

    private val activityScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val subscriptionInteractor = SubscriptionInteractor()
    private var billingProcessor: BillingProcessor? = null

    private val subscriptionObservers: MutableList<PremiumSubscriptionObserver> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        billingProcessor = BillingProcessor(this, LICENSE_KEY, this)
        billingProcessor?.initialize()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (billingProcessor == null || !billingProcessor!!.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        billingProcessor?.release()
        activityScope.cancel()
        super.onDestroy()
    }

    // Billing callbacks

    override fun onBillingInitialized() {
        activityScope.launch {
            var hasPremiumProducts = false
            withContext(Dispatchers.IO) {
                hasPremiumProducts = subscriptionInteractor.hasPremiumProducts()
            }
            if (hasPremiumProducts && !hasSubscription() && !isOpenedBuySubscribtionActivity) {
                val intent = BuySubscriptionActivity.getIntent(this@AbstractSubscriptionActivity)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onPurchaseHistoryRestored() {
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        subscriptionObservers.forEach { it.onSuccessfulSubscription() }
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        logException(this, error)
    }

    override fun hasSubscription(): Boolean {
        billingProcessor?.loadOwnedPurchasesFromGoogle()
        return billingProcessor?.isSubscribed(SUBSCRIPTION_ID) == true
    }

    override fun subscribe() {
        val isSubsUpdateSupported = billingProcessor?.isSubscriptionUpdateSupported == true
        if (isSubsUpdateSupported) {
            billingProcessor?.subscribe(this, SUBSCRIPTION_ID)
        } else {
            val message = resources.getString(R.string.billing_unavailable)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun observeSubscription(subscriptionObserver: PremiumSubscriptionObserver) {
        subscriptionObservers.add(subscriptionObserver)
    }

    override fun removeSubscriptionObserver(subscriptionObserver: PremiumSubscriptionObserver) {
        subscriptionObservers.remove(subscriptionObserver)
    }

    companion object {
        private const val SUBSCRIPTION_ID = "standard_subscription"
        private const val LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjqNWSexMmjxECoBCFKYAGTKAsesoYtvrxDk9uTDwg4A+sBA5YD7rvKBLEtQ2bfMLlKb0FQg6PZvmABtkb8oKAUEZCPVGuE6Ep4/pxWa/JO0Lz0mvZsLuh+8Obi8Bm1I3WG2kStaaeW+rYmA0r7m0vgd6XMa0Jl2ZImF+VFwcjHdL1wnik6WZNEMcme/Czvkxz06xADeapX7AocW7AMvgxFvbHdVqXTSoSzspfeNPB+/8745CN6B6HU4NlxCODjwkg3msYPROqNbO1rnoaWs4JBk+d6G67RYcsYxrJyZZYPNB6sKEZPjcokzwdDrcZ8fMsZF9j5jwpPdSR4epvwnWowIDAQAB"

        var isOpenedBuySubscribtionActivity = false
    }
}