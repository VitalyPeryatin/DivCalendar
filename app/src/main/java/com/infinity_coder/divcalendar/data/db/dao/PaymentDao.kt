package com.infinity_coder.divcalendar.data.db.dao

import androidx.room.*
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import com.infinity_coder.divcalendar.domain._common.convertStingToDate
import com.infinity_coder.divcalendar.domain._common.getNowDate
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
abstract class PaymentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertPastPayments(payments: List<PaymentDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFeaturePayments(payments: List<PaymentDbModel>)

    @Transaction
    open suspend fun insert(payments: List<PaymentDbModel>) {
        val todayDate = getNowDate()
        val pastPayments = payments.filter { isPastDate(todayDate, it.date) }
        val featurePayments = payments.filterNot { isPastDate(todayDate, it.date) }

        insertPastPayments(pastPayments)
        insertFeaturePayments(featurePayments)
    }

    private fun isPastDate(nowDate: Date, paymentDateStr: String): Boolean {
        val paymentDate = convertStingToDate(paymentDateStr)
        return paymentDate.before(nowDate)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun updatePayment(payment: PaymentDbModel)

    @Query("SELECT * FROM ${PaymentDbModel.TABLE_NAME} WHERE ${PaymentDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId")
    abstract fun getPaymentsFlow(portfolioId: Long): Flow<List<PaymentDbModel>>

    @Query("SELECT * FROM ${PaymentDbModel.TABLE_NAME} WHERE ${PaymentDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId")
    abstract suspend fun getPayments(portfolioId: Long): List<PaymentDbModel>

    @Query("SELECT * FROM ${SecurityDbModel.TABLE_NAME} WHERE ${SecurityDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId AND ${SecurityDbModel.COLUMN_ISIN} = :isin")
    abstract suspend fun getSecurity(portfolioId: Long, isin: String): SecurityDbModel?

    @Query("SELECT * FROM ${PaymentDbModel.TABLE_NAME} WHERE ${PaymentDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId AND ${PaymentDbModel.COLUMN_ISIN} = :isin AND ${PaymentDbModel.COLUMN_DATE} = :date")
    abstract suspend fun getPayment(portfolioId: Long, isin: String, date: String): PaymentDbModel

    @Transaction
    open suspend fun getPaymentsWithSecurity(portfolioId: Long, startDate: String, endDate: String): List<PaymentDbModel> {
        val startDateTime = DateFormatter.basicDateFormat.parse(startDate)!!
        val endDateTime = DateFormatter.basicDateFormat.parse(endDate)!!
        return getPayments(portfolioId).filter {
            val dateTime = DateFormatter.basicDateFormat.parse(it.date)!!
            dateTime.after(startDateTime) && dateTime.before(endDateTime)
        }.map {
            it.security = getSecurity(portfolioId, it.isin)
            it.dividends = it.dividends * (it.count ?: it.security?.count ?: 0)
            it
        }
    }
}