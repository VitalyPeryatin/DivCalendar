package com.infinity_coder.divcalendar.data.db.dao

import androidx.room.*
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import com.infinity_coder.divcalendar.domain._common.isExpiredDate
import java.math.BigDecimal
import java.math.MathContext

@Dao
abstract class PaymentDao {

    @Transaction
    open suspend fun insert(payments: List<PaymentDbModel>) {
        val pastPayments = payments.filter { isExpiredDate(it.date) }
        val futurePayments = payments.filterNot { isExpiredDate(it.date) }

        insertPastPayments(pastPayments)
        insertFuturePayments(futurePayments)
    }

    @Transaction
    open suspend fun deletePaymentsByDate(portfolioId: Long, date: String) {
        val endDateTime = DateFormatter.basicDateFormat.parse(date)

        val pastPayments = getPayments(portfolioId).filter {
            val dateTime = DateFormatter.basicDateFormat.parse(it.date)!!
            dateTime.before(endDateTime)
        }

        deletePayments(pastPayments)
    }

    @Delete
    abstract suspend fun deletePayments(payments: List<PaymentDbModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertPastPayments(payments: List<PaymentDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFuturePayments(payments: List<PaymentDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun updatePayment(payment: PaymentDbModel)

    @Query("SELECT * FROM ${PaymentDbModel.TABLE_NAME} WHERE ${PaymentDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId AND ${PaymentDbModel.COLUMN_ISIN} = :isin AND ${PaymentDbModel.COLUMN_DATE} = :date")
    abstract suspend fun getPayment(portfolioId: Long, isin: String, date: String): PaymentDbModel

    @Transaction
    open suspend fun getPaymentsWithSecurity(portfolioId: Long, startDate: String, endDate: String): List<PaymentDbModel> {
        val startDateTime = DateFormatter.basicDateFormat.parse(startDate)!!
        val endDateTime = DateFormatter.basicDateFormat.parse(endDate)!!

        val payments = getPayments(portfolioId).filter {
            val dateTime = DateFormatter.basicDateFormat.parse(it.date)!!
            dateTime.after(startDateTime) && dateTime.before(endDateTime)
        }
        preparedPayments(portfolioId, payments)
        return payments
    }

    @Transaction
    open suspend fun getAllPaymentsWithSecurity(portfolioId: Long): List<PaymentDbModel> {
        val payments = getPayments(portfolioId)
        preparedPayments(portfolioId, payments)
        return payments
    }

    private suspend fun preparedPayments(portfolioId: Long, payments: List<PaymentDbModel>) {
        payments.forEach {
            it.security = getSecurity(portfolioId, it.isin)
            if (it.count == BigDecimal.ZERO)
                it.count = it.security?.count!!
            it.dividends = it.dividends.multiply(it.count, MathContext.DECIMAL128)
        }
    }

    @Query("SELECT * FROM ${SecurityDbModel.TABLE_NAME} WHERE ${SecurityDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId AND ${SecurityDbModel.COLUMN_ISIN} = :isin")
    abstract suspend fun getSecurity(portfolioId: Long, isin: String): SecurityDbModel?

    @Query("SELECT * FROM ${PaymentDbModel.TABLE_NAME} WHERE ${PaymentDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId")
    abstract suspend fun getPayments(portfolioId: Long): List<PaymentDbModel>
}