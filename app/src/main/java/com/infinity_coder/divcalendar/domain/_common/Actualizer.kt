package com.infinity_coder.divcalendar.domain._common

import com.infinity_coder.divcalendar.data.repositories.RateRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Actualizer {

    private const val OUT_DATE_LIMIT = 30 * 60 * 1000

    private val subscriptions = mutableListOf<Subscription>()

    init {
        subscribe(RateRepository::updateRates, OUT_DATE_LIMIT.toLong())
    }

    fun actualize() = GlobalScope.launch{
       subscriptions.filter { System.currentTimeMillis() - it.lastUpdateMillis > it.outDateLimit }
           .forEach {
               it.lastUpdateMillis = System.currentTimeMillis()
               it.updateFunction.invoke()
           }
    }

    private fun subscribe(updateFunction:suspend ()->Unit, outDateLimit:Long){
        subscriptions.add(Subscription(updateFunction, outDateLimit))
    }

    data class Subscription(
        val updateFunction: suspend ()->Unit,
        val outDateLimit: Long,
        var lastUpdateMillis:Long = 0L
    )
}
