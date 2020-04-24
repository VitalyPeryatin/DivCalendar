package com.infinity_coder.divcalendar.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel.Companion.COLUMN_PORTFOLIO_ID
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel.Companion.COLUMN_SEC_ID
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel.Companion.INDEX_PORTFOLIO_ID
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel.Companion.TABLE_NAME
import com.infinity_coder.divcalendar.presentation._common.SecurityTypeDelegate

@Entity(
    tableName = TABLE_NAME,
    primaryKeys = [COLUMN_SEC_ID, COLUMN_PORTFOLIO_ID],
    foreignKeys = [ForeignKey(
        entity = PortfolioDbModel::class,
        parentColumns = arrayOf(PortfolioDbModel.COLUMN_ID),
        childColumns = arrayOf(COLUMN_PORTFOLIO_ID),
        onDelete = CASCADE
    )],
    indices = [Index(value = [PaymentDbModel.COLUMN_PORTFOLIO_ID], name = INDEX_PORTFOLIO_ID)]
)
data class SecurityDbModel(

    @ColumnInfo(name = COLUMN_SEC_ID)
    val ticker: String,

    @ColumnInfo(name = COLUMN_NAME)
    val name: String,

    @ColumnInfo(name = COLUMN_TYPE)
    var type: String = SecurityTypeDelegate.SECURITY_TYPE_STOCK,

    @ColumnInfo(name = COLUMN_LOGO)
    val logo: String = "",

    @ColumnInfo(name = COLUMN_YEAR_YIELD)
    var yearYield: Float = 0f,

    @ColumnInfo(name = COLUMN_EXCHANGE)
    var exchange: String? = null,

    @ColumnInfo(name = COLUMN_CURRENCY)
    var currency: String = "",

    @ColumnInfo(name = COLUMN_COUNT)
    var count: Int = 0,

    @ColumnInfo(name = COLUMN_TOTAL_PRICE)
    var totalPrice: Float = 0f,

    @ColumnInfo(name = COLUMN_PORTFOLIO_ID)
    var portfolioId: Long = 0,

    @ColumnInfo(name = COLUMN_COLOR)
    var color: Int = 0
) {
    companion object {
        const val TABLE_NAME = "Security"

        const val COLUMN_SEC_ID = "ticker"
        const val COLUMN_NAME = "name"
        const val COLUMN_LOGO = "logo"
        const val COLUMN_COUNT = "count"
        const val COLUMN_TOTAL_PRICE = "total_price"
        const val COLUMN_YEAR_YIELD = "year_yield"
        const val COLUMN_TYPE = "type"
        const val COLUMN_PORTFOLIO_ID = "portfolio_id"
        const val COLUMN_CURRENCY = "currency"
        const val COLUMN_EXCHANGE = "exchange"
        const val COLUMN_COLOR = "color"

        const val INDEX_PORTFOLIO_ID = "portfolio_id_index"
    }
}