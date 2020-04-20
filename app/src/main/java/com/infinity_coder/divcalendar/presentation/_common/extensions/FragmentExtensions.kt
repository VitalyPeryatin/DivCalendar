package com.infinity_coder.divcalendar.presentation._common.extensions

import androidx.fragment.app.Fragment
import com.infinity_coder.divcalendar.presentation.billing.PremiumSubscriptionObservable
import com.infinity_coder.divcalendar.presentation.billing.dialogs.BuySubscriptionDialog

fun Fragment.executeIfSubscribed(func: () -> Unit) {
    val premiumSubscriptionObservable = activity as? PremiumSubscriptionObservable ?: return
    if (premiumSubscriptionObservable.hasSubscription()) {
        func()
    } else {
        val dialog = BuySubscriptionDialog.newInstance()
        dialog.show(childFragmentManager, BuySubscriptionDialog::class.toString())
    }
}