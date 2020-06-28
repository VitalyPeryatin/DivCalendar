package com.infinity_coder.divcalendar.data.db.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel.Companion.COLUMN_DATE
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel.Companion.COLUMN_EXCHANGE
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel.Companion.COLUMN_ISIN
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel.Companion.COLUMN_PORTFOLIO_ID
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel.Companion.INDEX_SECURITY
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel.Companion.TABLE_NAME
import com.infinity_coder.divcalendar.data.network.model.PaymentNetModel
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Entity(
    tableName = TABLE_NAME,
    primaryKeys = [COLUMN_DATE, COLUMN_ISIN, COLUMN_PORTFOLIO_ID, COLUMN_EXCHANGE],
    foreignKeys = [ForeignKey(
        entity = SecurityDbModel::class,
        parentColumns = arrayOf(SecurityDbModel.COLUMN_ISIN, SecurityDbModel.COLUMN_PORTFOLIO_ID, SecurityDbModel.COLUMN_EXCHANGE),
        childColumns = arrayOf(COLUMN_ISIN, COLUMN_PORTFOLIO_ID, COLUMN_EXCHANGE),
        onDelete = CASCADE
    )],
    indices = [Index(value = [COLUMN_ISIN, COLUMN_PORTFOLIO_ID, COLUMN_EXCHANGE], name = INDEX_SECURITY)]
)
data class PaymentDbModel(
    @ColumnInfo(name = COLUMN_DIVIDENDS)
    var dividends: BigDecimal,

    @ColumnInfo(name = COLUMN_DATE)
    val date: String,

    @ColumnInfo(name = COLUMN_FORECAST)
    val forecast: Boolean,

    @ColumnInfo(name = COLUMN_ISIN)
    val isin: String,

    @ColumnInfo(name = COLUMN_PORTFOLIO_ID)
    val portfolioId: Long,

    @ColumnInfo(name = COLUMN_EXCHANGE, defaultValue = "")
    var exchange: String = "",

    @ColumnInfo(name = COLUMN_COUNT)
    var count: BigDecimal? = null
) {
    @Ignore
    var security: SecurityDbModel? = null

    companion object {
        const val TABLE_NAME = "Payment"

        const val COLUMN_DIVIDENDS = "dividends"
        const val COLUMN_DATE = "date"
        const val COLUMN_FORECAST = "forecast"
        const val COLUMN_ISIN = "isin"
        const val COLUMN_PORTFOLIO_ID = "portfolio_id"
        const val COLUMN_COUNT = "count"
        const val COLUMN_EXCHANGE = "exchange"

        const val INDEX_SECURITY = "security_index"

        fun from(portfolioId: Long, exchange: String, networkPayments: PaymentNetModel.Response) = PaymentDbModel(
            dividends = BigDecimal(networkPayments.dividends),
            date = networkPayments.date,
            forecast = networkPayments.forecast,
            isin = if (networkPayments.isin.isBlank()) networkPayments.name else networkPayments.isin,
            portfolioId = portfolioId,
            exchange = exchange
        )
    }
}