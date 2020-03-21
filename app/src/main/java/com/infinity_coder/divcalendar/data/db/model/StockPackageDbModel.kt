package com.infinity_coder.divcalendar.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinity_coder.divcalendar.data.db.model.StockPackageDbModel.Companion.TABLE_NAME
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel

@Entity(tableName = TABLE_NAME)
data class StockPackageDbModel(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_SEC_ID)
    val secid: String,

    @ColumnInfo(name = COLUMN_NAME)
    val name: String,

    val count: Int
) {
    companion object {
        const val TABLE_NAME = "StockPackage"

        const val COLUMN_SEC_ID = "sec_id"
        const val COLUMN_NAME = "name"

        fun from(shortStock: ShortStockNetworkModel) = StockPackageDbModel(
            secid = shortStock.secid,
            name = shortStock.name,
            count = 0
        )
    }
}