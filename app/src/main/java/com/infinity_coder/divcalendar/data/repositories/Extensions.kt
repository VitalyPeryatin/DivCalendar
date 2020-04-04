package com.infinity_coder.divcalendar.data.repositories

import kotlinx.coroutines.flow.FlowCollector
import java.lang.IllegalStateException

suspend fun <T> FlowCollector<T>.emitIf(value: T, throwable: Throwable = IllegalStateException(), condition: () -> Boolean) {
    if (condition()) emit(value) else throw throwable
}