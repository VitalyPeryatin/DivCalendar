package com.infinity_coder.divcalendar.domain._common

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ClickCounter(private val threshold: Int, private val maxClickAwait: Long) {

    private val rootJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Unconfined + rootJob)

    private var clickCounter = 0
    private var resetClickCounterJob: Job? = null
    private val mutex = Mutex()
    private var subscriber: (() -> Unit)? = null

    fun click() {
        scope.launch {
            mutex.withLock {
                if (clickCounter < threshold) {
                    increaseClickCounter()
                }
                if (clickCounter == threshold) {
                    clickCounter = 0
                    subscriber?.invoke()
                }
            }
        }
    }

    fun addSubscriber(func: () -> Unit) {
        this.subscriber = func
    }

    private fun increaseClickCounter() {
        resetClickCounterJob?.cancel()
        clickCounter += 1
        resetClickCounterJob = GlobalScope.launch(Dispatchers.IO) {
            delay(maxClickAwait)
            if (resetClickCounterJob != null && !resetClickCounterJob!!.isCancelled) {
                clickCounter = 0
            }
        }
    }
}