package com.infinity_coder.divcalendar.presentation._common.extensions

import android.util.Log
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
        dialog.show(childFragmentManager, BuySubscriptionDialog::class.toString())
    }
}

fun FragmentManager.hideAllFragments(ft: FragmentTransaction) {
    fragments.forEach {
        Log.d("RemoveFragmentLog",it.toString())
        ft.hide(it)
    }
}

fun Fragment.isAddedFragmentManager(fm: FragmentManager): Boolean {
    fm.fragments.forEach {
        if (it == this)
            return true
    }
    return false
}