package com.infinity_coder.divcalendar.data.repositories

import kotlinx.coroutines.flow.FlowCollector

suspend fun <T> FlowCollector<T>.emitIf(
    value: T,
    throwable: Throwable = IllegalStateException(),
    condition: () -> Boolean
) {
    if (condition()) emit(value) else throw throwable
}

suspend fun <T> FlowCollector<T>.emitIf(
    value: T,
    condition: () -> Boolean
) {
    if (condition()) emit(value)
}