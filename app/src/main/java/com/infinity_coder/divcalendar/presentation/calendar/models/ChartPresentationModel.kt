package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.domain.models.MonthlyPayment

data class ChartPresentationModel(
    val annualIncome: Float,
    val annualYield: Float,
    val currentCurrency: String,
    val monthlyPayments: List<MonthlyPayment>,
    val colors: List<Int>,
    val id: Int = 0
) : IComparableItem {
    override fun id() = id

    override fun content() = this
}