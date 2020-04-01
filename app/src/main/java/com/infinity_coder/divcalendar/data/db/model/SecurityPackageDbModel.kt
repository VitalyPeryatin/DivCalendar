package com.infinity_coder.divcalendar.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel.Companion.TABLE_NAME
import com.infinity_coder.divcalendar.presentation._common.SecurityTypeDelegate

@Entity(tableName = TABLE_NAME)
data class SecurityPackageDbModel(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_SEC_ID)
    val secid: String,

    @ColumnInfo(name = COLUMN_NAME)
    val name: String,

    @ColumnInfo(name = COLUMN_COUNT)
    var count: Int = 0,

    @ColumnInfo(name = COLUMN_TOTAL_PRICE)
    var totalPrice: Float = 0f,

    @ColumnInfo(name = COLUMN_YEAR_YIELD)
    var yearYield: Float = 0f,

    @ColumnInfo(name = COLUMN_TYPE)
    var type: String = SecurityTypeDelegate.SEC_TYP_STOCK
) {
    companion object {
        const val TABLE_NAME = "SecPackage"

        const val COLUMN_SEC_ID = "sec_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_COUNT = "count"
        const val COLUMN_TOTAL_PRICE = "total_price"
        const val COLUMN_YEAR_YIELD = "year_yield"
        const val COLUMN_TYPE = "type"
    }
}