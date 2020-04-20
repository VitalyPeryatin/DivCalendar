package com.infinity_coder.divcalendar.data.db.dao

import androidx.room.*
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PaymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(payments: List<PaymentDbModel>)

    @Query("SELECT * FROM ${PaymentDbModel.TABLE_NAME} WHERE ${PaymentDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId")
    abstract fun getPaymentsFlow(portfolioId: Long): Flow<List<PaymentDbModel>>

    @Query("SELECT * FROM ${PaymentDbModel.TABLE_NAME} WHERE ${PaymentDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId")
    abstract suspend fun getPayments(portfolioId: Long): List<PaymentDbModel>

    @Query("SELECT * FROM ${SecurityDbModel.TABLE_NAME} WHERE ${SecurityDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId AND ${SecurityDbModel.COLUMN_SEC_ID} = :ticker")
    abstract suspend fun getSecurity(portfolioId: Long, ticker: String): SecurityDbModel?

    @Transaction
    open suspend fun getPaymentsWithSecurity(portfolioId: Long, startDate: String, endDate: String): List<PaymentDbModel> {
        val startDateTime = DateFormatter.basicDateFormat.parse(startDate)!!
        val endDateTime = DateFormatter.basicDateFormat.parse(endDate)!!
        return getPayments(portfolioId).filter {
            val dateTime = DateFormatter.basicDateFormat.parse(it.date)!!
            dateTime.after(startDateTime) && dateTime.before(endDateTime)
        }.map {
            it.security = getSecurity(portfolioId, it.ticker)
            it
        }
    }
}