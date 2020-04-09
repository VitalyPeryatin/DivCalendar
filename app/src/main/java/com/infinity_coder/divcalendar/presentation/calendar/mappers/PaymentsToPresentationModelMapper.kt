package com.infinity_coder.divcalendar.presentation.calendar.mappers

import android.graphics.Color
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.domain.RateInteractor
import com.infinity_coder.divcalendar.domain.models.MonthlyPayment
import com.infinity_coder.divcalendar.presentation.calendar.models.*

class PaymentsToPresentationModelMapper {

    private val rateInteractor = RateInteractor()

    suspend fun mapToPresentationModel(monthlyPayments: List<MonthlyPayment>): List<IComparableItem> {
        val items = mutableListOf<IComparableItem>()

        val preparedAllMonthlyPayments = prepareAllMonthlyPayments(monthlyPayments)

        for (i in preparedAllMonthlyPayments.indices) {
            items.add(HeaderPaymentPresentationModel.from(preparedAllMonthlyPayments[i]))

            val preparedMonthlyPayment = prepareMonthlyPayment(preparedAllMonthlyPayments[i])
            items.addAll(preparedMonthlyPayment)

            val preparedTotalMonthPayment = prepareTotalMonthPayments(preparedAllMonthlyPayments[i])
            items.add(preparedTotalMonthPayment)

            if (i != preparedAllMonthlyPayments.lastIndex) {
                items.add(DividerPresentationModel)
            }
        }
        items.add(0, mapPaymentsToChartPresentationModel(preparedAllMonthlyPayments))
        return items
    }

    private suspend fun prepareAllMonthlyPayments(monthlyPayments: List<MonthlyPayment>): List<MonthlyPayment> {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        return monthlyPayments.map { monthlyPayment ->
            val payments = monthlyPayment.payments.map {
                it.copy(dividends = rateInteractor.convertCurrencies(it.dividends.toFloat(), it.currency, currentCurrency).toDouble())
            }
            return@map MonthlyPayment(monthlyPayment.month, payments)
        }
    }

    private fun prepareMonthlyPayment(monthlyPayment: MonthlyPayment): List<PaymentPresentationModel> {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        val paymentsForMonth = PaymentPresentationModel.from(monthlyPayment)
        paymentsForMonth.forEach {
            it.currentCurrency = currentCurrency
        }
        return paymentsForMonth
    }

    private fun prepareTotalMonthPayments(monthlyPayment: MonthlyPayment): FooterPaymentPresentationModel {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        val totalPayments = FooterPaymentPresentationModel.from(monthlyPayment)
        totalPayments.currentCurrency = currentCurrency
        return totalPayments
    }

    private fun mapPaymentsToChartPresentationModel(monthlyPayments: List<MonthlyPayment>): ChartPresentationModel {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        val annualIncome = sumMonthPayments(monthlyPayments)
        val annualYield = 0f
        val allMonthlyPayments = getMonthlyPayments(monthlyPayments)
        val colors = getChartBarColors(allMonthlyPayments)
        return ChartPresentationModel(annualIncome, annualYield, currentCurrency, allMonthlyPayments, colors)
    }

    private fun sumMonthPayments(monthlyPayments: List<MonthlyPayment>): Float {
        return monthlyPayments.sumByDouble { paymentsForMonth ->
            paymentsForMonth.payments.sumByDouble { it.dividends }
        }.toFloat()
    }

    private fun getMonthlyPayments(monthlyPayments: List<MonthlyPayment>): List<MonthlyPayment> {
        return (0..11).map { numberMonth ->
            val payments = monthlyPayments.find { it.month == numberMonth }?.payments ?: listOf()
            return@map MonthlyPayment(numberMonth, payments)
        }
    }

    private fun getChartBarColors(monthlyPayments: List<MonthlyPayment>): List<Int> {
        return monthlyPayments.map {
            return@map if (it.payments.isEmpty())
                listOf(Color.TRANSPARENT)
            else
                it.payments.map { payment -> payment.colorLogo }
        }.flatten()
    }
}