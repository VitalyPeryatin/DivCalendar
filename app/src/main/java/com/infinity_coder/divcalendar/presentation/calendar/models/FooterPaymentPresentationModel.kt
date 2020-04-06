package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.domain.models.PaymentsForMonth

data class FooterPaymentPresentationModel(
    val id: Int,
    val income: Double,
    val originalCurrency: String,
    var currentCurrency: String
) : IComparableItem {

    companion object {
        fun from(monthlyPayments: PaymentsForMonth) =
            FooterPaymentPresentationModel(
                id = monthlyPayments.month,
                income = monthlyPayments.payments.sumByDouble { payment -> payment.dividends },
                originalCurrency = monthlyPayments.payments.first().currency,
                currentCurrency = ""
            )
    }

    override fun id() = id

    override fun content() = this
}