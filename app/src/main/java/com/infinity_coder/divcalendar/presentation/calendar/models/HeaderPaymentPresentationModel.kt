package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.domain.models.PaymentsForMonth

data class HeaderPaymentPresentationModel(
    val month: Int
) : IComparableItem {

    companion object {
        fun from(monthlyPayments: PaymentsForMonth) =
            HeaderPaymentPresentationModel(
                month = monthlyPayments.month
            )
    }

    override fun id() = this

    override fun content() = this
}