package com.infinity_coder.divcalendar.presentation.buysubscription

interface PremiumSubscriptionObservable {
    fun hasSubscription(): Boolean

    fun subscribe()

    fun observeSubscription(subscriptionObserver: PremiumSubscriptionObserver)

    fun removeSubscriptionObserver(subscriptionObserver: PremiumSubscriptionObserver)
}