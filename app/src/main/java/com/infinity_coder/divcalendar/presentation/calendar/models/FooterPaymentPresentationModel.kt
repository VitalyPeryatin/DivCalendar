package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.domain.models.PaymentsForMonth

data class FooterPaymentPresentationModel(
    val id: Int,
    val income: Double
) : IComparableItem {

    companion object {
        fun from(monthlyPayments: PaymentsForMonth) =
            FooterPaymentPresentationModel(
                id = monthlyPayments.month,
                income = monthlyPayments.payments.sumByDouble { payment -> payment.dividends }
            )
    }

    override fun id() = id

    override fun content() = this
}