package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.presentation._common.extensions.sumByBigDecimal

data class FooterPaymentPresentationModel(
    val id: Int,
    var income: String,
    val originalCurrency: String,
    var currentCurrency: String
) : IComparableItem {

    companion object {
        fun from(monthlyPayments: MonthlyPayment) =
            FooterPaymentPresentationModel(
                id = monthlyPayments.month,
                income = monthlyPayments.payments.sumByBigDecimal { payment -> payment.dividends }.toPlainString(),
                originalCurrency = monthlyPayments.payments.first().security!!.currency,
                currentCurrency = ""
            )
    }

    override fun id() = id

    override fun content() = this
}