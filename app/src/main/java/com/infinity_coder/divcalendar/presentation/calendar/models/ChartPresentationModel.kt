package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel

data class ChartPresentationModel(
    val annualIncome: Float,
    val annualYield: Float,
    val currentCurrency: String,
    val monthlyPayments: List<Pair<Int, List<PaymentNetworkModel>>>,
    val colors: List<Int>
) : IComparableItem {
    override fun id() = this

    override fun content() = this
}