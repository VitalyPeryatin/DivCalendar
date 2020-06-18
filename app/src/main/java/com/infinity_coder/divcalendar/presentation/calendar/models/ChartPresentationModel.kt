package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import java.math.BigDecimal

data class ChartPresentationModel(
    val annualIncome: String,
    val annualYield: BigDecimal,
    val currentCurrency: String,
    val monthlyPayments: List<MonthlyPayment>,
    val colors: List<Int>,
    val id: Int = 0
) : IComparableItem {
    override fun id() = id

    override fun content() = this
}