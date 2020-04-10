package com.infinity_coder.divcalendar.domain

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.palette.graphics.Palette
import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel
import com.infinity_coder.divcalendar.data.repositories.PaymentRepository
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import com.infinity_coder.divcalendar.domain.models.MonthlyPayment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.net.URL
import java.util.*

class CalendarInteractor {

    private val dateFormat = DateFormatter.basicDateFormat

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getPayments(isIncludeTaxes: Boolean): Flow<List<MonthlyPayment>> {
        return PaymentRepository.loadAllPayments()
            .map { calculateTaxesIfNeed(isIncludeTaxes, it) }
            .map { groupAndSortPayments(it) }
    }

    private fun calculateTaxesIfNeed(isIncludeTaxes: Boolean, payments: List<PaymentNetworkModel>): List<PaymentNetworkModel> {
        if (isIncludeTaxes) {
            payments.forEach { it.dividends *= TAX_FACTOR }
        }
        return payments
    }

    private fun groupAndSortPayments(payments: List<PaymentNetworkModel>): List<MonthlyPayment> {
        return payments.groupByDate()
            .map(this::mapMonthWithPaymentsToMonthlyPayment)
            .sortedBy { it.month }
    }

    private fun List<PaymentNetworkModel>.groupByDate(): List<Pair<Int, List<PaymentNetworkModel>>> {
        return groupBy {
            Calendar.getInstance().apply { time = dateFormat.parse(it.date) ?: Date() }
                .get(Calendar.MONTH)
        }.toList()
    }

    private fun mapMonthWithPaymentsToMonthlyPayment(monthWithPayments: Pair<Int, List<PaymentNetworkModel>>): MonthlyPayment {
        val monthlyPayment = MonthlyPayment.from(monthWithPayments)
        monthlyPayment.payments.forEach {
            it.colorLogo = getDominantColorFromImage(it.logo)
        }
        return monthlyPayment
    }

    private fun getDominantColorFromImage(url: String): Int {
        return try {
            val inputStream: InputStream = URL(url).openStream()
            val image = BitmapFactory.decodeStream(inputStream)
            Palette.from(image).generate().getDominantColor(0)
        } catch (e: Exception) {
            Color.BLACK
        }
    }

    companion object {
        private const val TAX_FACTOR = 0.87
    }
}