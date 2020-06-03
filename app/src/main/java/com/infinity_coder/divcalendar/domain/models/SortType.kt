package com.infinity_coder.divcalendar.domain.models

sealed class SortType {
    object NextPayoutDate: SortType()
    object SizeOfNextPayout: SortType()
    object Profitability: SortType()
}