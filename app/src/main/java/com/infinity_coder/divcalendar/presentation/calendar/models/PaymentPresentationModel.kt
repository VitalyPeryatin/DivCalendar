package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel

data class PaymentPresentationModel(
    val isin: String = "",
    val name: String = "",
    val portfolioId: Long = 0L,
    val entityDate: String = "",
    val logo: String = "",
    val count: Int = 0,
    var dividends: String = "",
    var date: String = "",
    val forecast: Boolean = false,
    val colorLogo: Int = 0,
    val originalCurrency: String = "",
    var currentCurrency: String = "",
    var expired: Boolean = false
) : IComparableItem {

    companion object {
        fun from(monthlyPayments: MonthlyPayment) =
            monthlyPayments.payments.map { from(it) }

        private fun from(payment: PaymentDbModel): PaymentPresentationModel {
            return payment.security?.let { security ->
                PaymentPresentationModel(
                    isin = security.isin,
                    name = security.name,
                    portfolioId = security.portfolioId,
                    entityDate = payment.date,
                    logo = security.logo,
                    count = payment.count ?: security.count,
                    dividends = payment.dividends.toString(),
                    date = payment.date,
                    forecast = payment.forecast,
                    colorLogo = security.color,
                    originalCurrency = security.currency,
                    currentCurrency = "",
                    expired = false
                )
            } ?: PaymentPresentationModel()
        }
    }

    override fun id() = name + date

    override fun content() = this
}