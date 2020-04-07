package com.infinity_coder.divcalendar.presentation.calendar.mappers

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.palette.graphics.Palette
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.domain.RateInteractor
import com.infinity_coder.divcalendar.domain.models.MonthlyPayment
import com.infinity_coder.divcalendar.presentation.calendar.models.*
import java.io.InputStream
import java.net.URL

class PaymentsToPresentationModelMapper {

    private val rateInteractor = RateInteractor()

    suspend fun mapToPresentationModel(monthlyPayments: List<MonthlyPayment>): List<IComparableItem> {
        val items = mutableListOf<IComparableItem>()

        for (i in monthlyPayments.indices) {
            items.add(HeaderPaymentPresentationModel.from(monthlyPayments[i]))

            val preparedMonthlyPayments = prepareMonthlyPayments(monthlyPayments[i])
            items.addAll(preparedMonthlyPayments)

            val preparedTotalMonthPayments = prepareTotalMonthPayments(monthlyPayments[i])
            items.add(preparedTotalMonthPayments)

            if (i != monthlyPayments.lastIndex) {
                items.add(DividerPresentationModel)
            }
        }
        items.add(0, mapPaymentsToChartPresentationModel(monthlyPayments))
        return items
    }
    
    private suspend fun prepareMonthlyPayments(monthlyPayment: MonthlyPayment): List<PaymentPresentationModel> {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        val paymentsForMonth = PaymentPresentationModel.from(monthlyPayment)
        paymentsForMonth.forEach {
            it.currentCurrency = currentCurrency
            it.dividends = rateInteractor.convertCurrencies(it.dividends.toFloat(), it.originalCurrency, currentCurrency).toDouble()
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
                it.payments.map { payment -> getDominantColorFromImage(payment.logo) }
        }.flatten()
    }

    private fun getDominantColorFromImage(url: String): Int {
        // TODO сделать обработку SVG
        return try {
            val inputStream: InputStream = URL(url).openStream()
            val image = BitmapFactory.decodeStream(inputStream)
            Palette.from(image).generate().getDominantColor(0)
        } catch (e: Exception) {
            Color.BLACK
        }
    }
}