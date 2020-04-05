package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.domain.models.PaymentsForMonth

data class ChartPresentationModel(
    val annualIncome: String,
    val monthlyPayments: List<PaymentsForMonth>,
    val colors: List<Int>
) : IComparableItem {
    override fun id() = this

    override fun content() = this
}