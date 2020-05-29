package com.infinity_coder.divcalendar.presentation._common.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.infinity_coder.divcalendar.presentation.billing.PremiumSubscriptionObservable
import com.infinity_coder.divcalendar.presentation.billing.dialogs.BuySubscriptionDialog

fun Fragment.executeIfSubscribed(func: () -> Unit) {
    val premiumSubscriptionObservable = activity as? PremiumSubscriptionObservable ?: return
    if (premiumSubscriptionObservable.hasSubscription()) {
        func()
    } else {
        val dialog = BuySubscriptionDialog.newInstance()
        dialog.show(childFragmentManager, BuySubscriptionDialog.TAG)
    }
}

fun FragmentManager.hideAllFragments(transaction: FragmentTransaction) {
    fragments.forEach {
        transaction.hide(it)
    }
}