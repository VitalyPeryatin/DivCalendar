package com.infinity_coder.divcalendar.data.repositories

import kotlinx.coroutines.flow.FlowCollector

suspend fun <T> FlowCollector<T>.emitIf(value: T, throwable: Throwable, condition: () -> Boolean) {
    if (condition()) emit(value) else throw throwable
}