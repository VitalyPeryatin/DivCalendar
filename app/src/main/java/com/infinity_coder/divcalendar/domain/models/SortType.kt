package com.infinity_coder.divcalendar.domain.models

sealed class SortType {
    object PaymentDate: SortType()
    object Profitability: SortType()
    object Alphabetically: SortType()
}