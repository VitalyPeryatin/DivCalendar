package com.infinity_coder.divcalendar.presentation.calendar.mappers

import android.content.Context
import android.graphics.Color
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.repositories.RateRepository.RUB_RATE
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import com.infinity_coder.divcalendar.domain.RateInteractor
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import com.infinity_coder.divcalendar.domain.models.MonthlyPayment
import com.infinity_coder.divcalendar.presentation._common.SecurityCurrencyDelegate
import com.infinity_coder.divcalendar.presentation.calendar.models.*
import kotlinx.coroutines.flow.first

class PaymentsToPresentationModelMapper {

    private val rateInteractor = RateInteractor()
    private val portfolioInteractor = PortfolioInteractor()

    suspend fun mapToPresentationModel(context: Context, monthlyPayments: List<MonthlyPayment>): List<IComparableItem> {
        val items = mutableListOf<IComparableItem>()

        if (monthlyPayments.isNotEmpty()) {
            val preparedAllMonthlyPayments = prepareAllMonthlyPayments(monthlyPayments)

            for (i in preparedAllMonthlyPayments.indices) {
                items.add(HeaderPaymentPresentationModel.from(preparedAllMonthlyPayments[i]))

                val preparedMonthlyPayment = prepareMonthlyPayment(context, preparedAllMonthlyPayments[i])
                items.addAll(preparedMonthlyPayment)

                val preparedTotalMonthPayment = prepareTotalMonthPayments(context, preparedAllMonthlyPayments[i])
                items.add(preparedTotalMonthPayment)

                if (i != preparedAllMonthlyPayments.lastIndex) {
                    items.add(DividerPresentationModel)
                }
            }
            items.add(0, mapPaymentsToChartPresentationModel(context, preparedAllMonthlyPayments))
        }

        return items
    }

    private suspend fun prepareAllMonthlyPayments(monthlyPayments: List<MonthlyPayment>): List<MonthlyPayment> {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        return monthlyPayments.map { monthlyPayment ->
            val payments = monthlyPayment.payments.map {
                val payment = it.copy(dividends = rateInteractor.convertCurrencies(it.dividends.toFloat(), it.security!!.currency, currentCurrency).toDouble())
                payment.security = it.security
                payment
            }
            return@map MonthlyPayment(monthlyPayment.month, payments)
        }
    }

    private fun prepareMonthlyPayment(context: Context, monthlyPayment: MonthlyPayment): List<PaymentPresentationModel> {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        val paymentsForMonth = PaymentPresentationModel.from(monthlyPayment)
        paymentsForMonth.forEach {
            it.currentCurrency = currentCurrency
            it.expired = DateFormatter.isExpired(it.date)
            it.date = context.getDate(it.date)
            it.dividends = SecurityCurrencyDelegate.getValueWithCurrency(context, it.dividends, it.currentCurrency)
        }
        return paymentsForMonth
    }

    private fun Context.getDate(date: String): String {
        val months = resources.getStringArray(R.array.months_genitive)
        return DateFormatter.formatDate(date, months)
    }

    private fun prepareTotalMonthPayments(context: Context, monthlyPayment: MonthlyPayment): FooterPaymentPresentationModel {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        val totalPayments = FooterPaymentPresentationModel.from(monthlyPayment)
        totalPayments.currentCurrency = currentCurrency
        totalPayments.income = SecurityCurrencyDelegate.getValueWithCurrency(context, totalPayments.income, totalPayments.currentCurrency)
        return totalPayments
    }

    private suspend fun mapPaymentsToChartPresentationModel(context: Context, monthlyPayments: List<MonthlyPayment>): ChartPresentationModel {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        val annualIncome = sumMonthPayments(monthlyPayments)
        val annualYield = (annualIncome / getCosts()) * 100
        val annualIncomeStr = SecurityCurrencyDelegate.getValueWithCurrency(context, annualIncome, currentCurrency)
        val allMonthlyPayments = getMonthlyPayments(monthlyPayments)
        val colors = getChartBarColors(allMonthlyPayments)
        return ChartPresentationModel(annualIncomeStr, annualYield, currentCurrency, allMonthlyPayments, colors)
    }

    private fun sumMonthPayments(monthlyPayments: List<MonthlyPayment>): Float {
        return monthlyPayments.sumByDouble { paymentsForMonth ->
            paymentsForMonth.payments.sumByDouble { it.dividends }
        }.toFloat()
    }

    private suspend fun getCosts(): Float {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        val securities = portfolioInteractor.getCurrentPortfolio().first().securities
        return securities.sumByDouble{
            getTotalPriceForCurrentCurrency(currentCurrency,it)
        }.toFloat()
    }

    private suspend fun getTotalPriceForCurrentCurrency(currentCurrency:String, securityDbModel: SecurityDbModel):Double{
        return if(securityDbModel.currency == currentCurrency)
            securityDbModel.totalPrice.toDouble()
        else
            rateInteractor.convertCurrencies(securityDbModel.totalPrice, securityDbModel.currency, currentCurrency).toDouble()
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