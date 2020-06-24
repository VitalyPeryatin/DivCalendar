package com.infinity_coder.divcalendar.presentation.tabs.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem

data class ChartPresentationModel(
    val annualIncome: String,
    val annualYield: Double,
    val currentCurrency: String,
    val monthlyPayments: List<MonthlyPayment>,
    val colors: List<Int>,
    val id: Int = 0
) : IComparableItem {
    override fun id() = id

    override fun content() = this
}