package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.domain.models.MonthlyPayment
import com.infinity_coder.divcalendar.domain.models.Payment

data class PaymentPresentationModel(
    val name: String,
    val logo: String,
    val count: Int,
    var dividends: String,
    var date: String,
    val colorLogo: Int,
    val originalCurrency: String,
    var currentCurrency: String,
    var expired: Boolean
) : IComparableItem {

    companion object {
        fun from(monthlyPayments: MonthlyPayment) =
            monthlyPayments.payments.map { from(it) }

        private fun from(payment: Payment) =
            PaymentPresentationModel(
                name = payment.name,
                logo = payment.logo,
                count = payment.count,
                dividends = payment.dividends.toString(),
                date = payment.date,
                colorLogo = payment.colorLogo,
                originalCurrency = payment.currency,
                currentCurrency = "",
                expired = false
            )
    }

    override fun id() = name + date

    override fun content() = this
}