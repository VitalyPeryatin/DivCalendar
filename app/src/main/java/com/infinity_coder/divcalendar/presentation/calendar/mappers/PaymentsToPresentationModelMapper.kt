package com.infinity_coder.divcalendar.presentation.calendar.mappers

import android.graphics.Color
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import com.infinity_coder.divcalendar.domain.RateInteractor
import com.infinity_coder.divcalendar.domain._common.isExpiredDate
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityCurrencyDelegate
import com.infinity_coder.divcalendar.presentation._common.extensions.sumByBigDecimal
import com.infinity_coder.divcalendar.presentation.calendar.models.*
import kotlinx.coroutines.flow.first
import java.math.BigDecimal

class PaymentsToPresentationModelMapper {

    private val rateInteractor = RateInteractor()
    private val portfolioInteractor = PortfolioInteractor()

    suspend fun mapToPresentationModel(monthlyPayments: List<MonthlyPayment>): List<IComparableItem> {
        val items = mutableListOf<IComparableItem>()

        if (monthlyPayments.isNotEmpty()) {
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
        }

        return items
    }

    private suspend fun prepareAllMonthlyPayments(monthlyPayments: List<MonthlyPayment>): List<MonthlyPayment> {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        return monthlyPayments.map { monthlyPayment ->
            val payments = monthlyPayment.payments.map {
                val payment = it.copy(dividends = rateInteractor.convertCurrencies(it.dividends, it.security!!.currency, currentCurrency))
                payment.security = it.security
                payment
            }
            return@map MonthlyPayment(monthlyPayment.month, payments)
        }
    }

    private fun prepareMonthlyPayment(monthlyPayment: MonthlyPayment): List<PaymentPresentationModel> {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        val paymentsForMonth = PaymentPresentationModel.from(monthlyPayment)
        paymentsForMonth.forEach {
            it.currentCurrency = currentCurrency
            it.expired = isExpiredDate(it.presentationDate)
            it.dividends = SecurityCurrencyDelegate.getValueConsiderCopecks(it.dividends)
        }
        return paymentsForMonth
    }

    private fun prepareTotalMonthPayments(monthlyPayment: MonthlyPayment): FooterPaymentPresentationModel {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        val totalPayments = FooterPaymentPresentationModel.from(monthlyPayment)
        totalPayments.currentCurrency = currentCurrency
        totalPayments.income = SecurityCurrencyDelegate.getValueConsiderCopecks(totalPayments.income)
        return totalPayments
    }

    private suspend fun mapPaymentsToChartPresentationModel(monthlyPayments: List<MonthlyPayment>): ChartPresentationModel {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        val annualIncome = sumMonthPayments(monthlyPayments)
        val annualYield = (annualIncome.toPlainString().toDouble() / getCosts().toPlainString().toDouble()) * 100
        val annualIncomeStr = SecurityCurrencyDelegate.getValueConsiderCopecks(annualIncome)
        val allMonthlyPayments = getMonthlyPayments(monthlyPayments)
        val colors = getChartBarColors(allMonthlyPayments)
        return ChartPresentationModel(annualIncomeStr, annualYield, currentCurrency, allMonthlyPayments, colors)
    }

    private fun sumMonthPayments(monthlyPayments: List<MonthlyPayment>): BigDecimal {
        return monthlyPayments.sumByBigDecimal { paymentsForMonth ->
            paymentsForMonth.payments.sumByBigDecimal { it.dividends }
        }
    }

    private suspend fun getCosts(): BigDecimal {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        val securities = portfolioInteractor.getCurrentPortfolioFlow().first().securities
        return securities.sumByBigDecimal {
            getTotalPriceForCurrentCurrency(currentCurrency, it)
        }
    }

    private suspend fun getTotalPriceForCurrentCurrency(currentCurrency: String, securityDbModel: SecurityDbModel): BigDecimal {
        return if (securityDbModel.currency == currentCurrency)
            securityDbModel.totalPrice
        else
            rateInteractor.convertCurrencies(securityDbModel.totalPrice, securityDbModel.currency, currentCurrency)
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
                it.payments.map { payment -> payment.security!!.color }
        }.flatten()
    }
}