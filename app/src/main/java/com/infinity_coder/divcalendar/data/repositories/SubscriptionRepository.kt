package com.infinity_coder.divcalendar.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.infinity_coder.divcalendar.presentation.App

object SubscriptionRepository {
    private const val SUBSCRIPTION_PREFERENCES_NAME = "SubscriptionFile"
    private const val PREF_HAS_SUBSCRIPTION = "has_subscription"

    private val subscriptionPreferences = App.instance.getSharedPreferences(SUBSCRIPTION_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun saveHasSubscription(hasSubscription: Boolean) {
        subscriptionPreferences.edit {
            putBoolean(PREF_HAS_SUBSCRIPTION, hasSubscription)
        }
    }

    fun hasSubscription(): Boolean {
        return subscriptionPreferences.getBoolean(PREF_HAS_SUBSCRIPTION, false)
    }
}