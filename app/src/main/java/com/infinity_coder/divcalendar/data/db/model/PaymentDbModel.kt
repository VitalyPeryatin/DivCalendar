package com.infinity_coder.divcalendar.data.db.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel.Companion.COLUMN_DATE
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel.Companion.COLUMN_PORTFOLIO_ID
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel.Companion.COLUMN_TICKER
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel.Companion.INDEX_SECURITY
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel.Companion.TABLE_NAME
import com.infinity_coder.divcalendar.data.network.model.PaymentNetModel

@Entity(
    tableName = TABLE_NAME,
    primaryKeys = [COLUMN_DATE, COLUMN_TICKER, COLUMN_PORTFOLIO_ID],
    foreignKeys = [ForeignKey(
        entity = SecurityDbModel::class,
        parentColumns = arrayOf(SecurityDbModel.COLUMN_SEC_ID, SecurityDbModel.COLUMN_PORTFOLIO_ID),
        childColumns = arrayOf(COLUMN_TICKER, COLUMN_PORTFOLIO_ID),
        onDelete = CASCADE
    )],
    indices = [Index(value = [COLUMN_TICKER, COLUMN_PORTFOLIO_ID], name = INDEX_SECURITY)]
)
data class PaymentDbModel(
    @ColumnInfo(name = COLUMN_DIVIDENDS)
    var dividends: Double,

    @ColumnInfo(name = COLUMN_DATE)
    val date: String,

    @ColumnInfo(name = COLUMN_FORECAST)
    val forecast: String,

    @ColumnInfo(name = COLUMN_TICKER)
    val ticker: String,

    @ColumnInfo(name = COLUMN_PORTFOLIO_ID)
    val portfolioId: Long
) {
    @Ignore
    var security: SecurityDbModel? = null

    companion object {
        const val TABLE_NAME = "Payment"

        const val COLUMN_DIVIDENDS = "dividends"
        const val COLUMN_DATE = "date"
        const val COLUMN_FORECAST = "forecast"
        const val COLUMN_TICKER = "ticker"
        const val COLUMN_PORTFOLIO_ID = "portfolio_id"

        const val INDEX_SECURITY = "security_index"

        fun from(portfolioId: Long, networkPayments: PaymentNetModel.Response) = PaymentDbModel(
            dividends = networkPayments.dividends,
            date = networkPayments.date,
            forecast = networkPayments.forecast,
            ticker = networkPayments.ticker,
            portfolioId = portfolioId
        )

        fun from(portfolioId: Long, networkPayments: List<PaymentNetModel.Response>): List<PaymentDbModel> {
            return networkPayments.map { from(portfolioId, it) }
        }
    }
}