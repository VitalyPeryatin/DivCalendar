package com.infinity_coder.divcalendar.presentation.buysubscription

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.infinity_coder.divcalendar.R
import kotlinx.android.synthetic.main.activity_buy_subscription.*

class BuySubscriptionActivity : AppCompatActivity(), BillingProcessor.IBillingHandler {

    private val LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjqNWSexMmjxECoBCFKYAGTKAsesoYtvrxDk9uTDwg4A+sBA5YD7rvKBLEtQ2bfMLlKb0FQg6PZvmABtkb8oKAUEZCPVGuE6Ep4/pxWa/JO0Lz0mvZsLuh+8Obi8Bm1I3WG2kStaaeW+rYmA0r7m0vgd6XMa0Jl2ZImF+VFwcjHdL1wnik6WZNEMcme/Czvkxz06xADeapX7AocW7AMvgxFvbHdVqXTSoSzspfeNPB+/8745CN6B6HU4NlxCODjwkg3msYPROqNbO1rnoaWs4JBk+d6G67RYcsYxrJyZZYPNB6sKEZPjcokzwdDrcZ8fMsZF9j5jwpPdSR4epvwnWowIDAQAB"
    private var billingProcessor: BillingProcessor? = null
    private val subscriptionId = "standard_subscription"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_subscription)

        billingProcessor = BillingProcessor(this, LICENSE_KEY, this)
        billingProcessor?.initialize()

        buySubscriptionButton.setOnClickListener {
            subscribe()
        }
    }

    override fun onBillingInitialized() {
        showToast("BuySubscriptionActivity onBillingInitialized()")
        val isSubscribed = billingProcessor!!.isSubscribed(subscriptionId)
        showToast("BuySubscriptionActivity isSubscribed: $isSubscribed")
    }

    override fun onPurchaseHistoryRestored() {
        showToast("BuySubscriptionActivity onPurchaseHistoryRestored()")
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        showToast("BuySubscriptionActivity onProductPurchased($productId, ${details?.purchaseInfo})")
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        showToast("BuySubscriptionActivity onBillingError: errorCode = $errorCode")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun subscribe() {
        billingProcessor?.subscribe(this, subscriptionId)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, BuySubscriptionActivity::class.java)
        }
    }

}