package com.infinity_coder.divcalendar.domain._common

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Actualizer {

    private val subscriptions = mutableListOf<Subscription>()

    fun actualize() = GlobalScope.launch {
        subscriptions.filter { System.currentTimeMillis() - it.lastUpdateMillis > it.outDateLimit }
            .forEach {
                it.lastUpdateMillis = System.currentTimeMillis()
                it.updateFunction.invoke()
            }
    }

    fun subscribe(updateFunction: suspend () -> Unit, outDateLimit: Long) {
        subscriptions.add(Subscription(updateFunction, outDateLimit))
    }

    data class Subscription(
        val updateFunction: suspend () -> Unit,
        val outDateLimit: Long,
        var lastUpdateMillis: Long = 0L
    )
}