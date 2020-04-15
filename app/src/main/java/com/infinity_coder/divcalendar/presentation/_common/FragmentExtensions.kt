package com.infinity_coder.divcalendar.presentation._common

import androidx.fragment.app.Fragment
import com.infinity_coder.divcalendar.presentation.billing.BuySubscriptionDialog
import com.infinity_coder.divcalendar.presentation.billing.PremiumSubscriptionObservable

fun Fragment.executeIfSubscribed(func: () -> Unit) {
    val premiumSubscriptionObservable = activity as? PremiumSubscriptionObservable ?: return
    if (premiumSubscriptionObservable.hasSubscription()) {
        func()
    } else {
        val dialog = BuySubscriptionDialog.newInstance()
        dialog.show(childFragmentManager, BuySubscriptionDialog::class.toString())
    }
}