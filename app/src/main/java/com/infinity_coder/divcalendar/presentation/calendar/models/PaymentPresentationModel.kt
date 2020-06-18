package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel
import java.math.BigDecimal

data class PaymentPresentationModel(
    val isin: String = "",
    val name: String = "",
    val portfolioId: Long = 0L,
    val databaseFieldDate: String = "",
    val logo: String = "",
    val count: BigDecimal = BigDecimal.ZERO,
    var dividends: String = "",
    var presentationDate: String = "",
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
                    databaseFieldDate = payment.date,
                    logo = security.logo,
                    count = payment.count,
                    dividends = payment.dividends.toString(),
                    presentationDate = payment.date,
                    forecast = payment.forecast,
                    colorLogo = security.color,
                    originalCurrency = security.currency,
                    currentCurrency = "",
                    expired = false
                )
            } ?: PaymentPresentationModel()
        }
    }

    override fun id() = isin + presentationDate

    override fun content() = this
}