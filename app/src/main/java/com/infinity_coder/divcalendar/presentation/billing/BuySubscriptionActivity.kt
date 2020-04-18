package com.infinity_coder.divcalendar.presentation.billing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.anjlab.android.iab.v3.TransactionDetails
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.AbstractSubscriptionActivity
import com.infinity_coder.divcalendar.presentation.main.MainActivity
import kotlinx.android.synthetic.main.activity_buy_subscription.*

class BuySubscriptionActivity : AbstractSubscriptionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        isOpenedBuySubscribtionActivity = true

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_subscription)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        buySubscriptionButton.setOnClickListener { subscribe() }
        buySubscriptionButton2.setOnClickListener { subscribe() }
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        super.onProductPurchased(productId, details)

        if (hasSubscription()) {
            val intent = MainActivity.getIntent(this)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        isOpenedBuySubscribtionActivity = false
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (!hasSubscription()) {
            finish()
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, BuySubscriptionActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("EXIT", true)
            }
        }
    }
}