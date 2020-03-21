package com.infinity_coder.divcalendar.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinity_coder.divcalendar.data.db.model.ShortStockDbModel.Companion.TABLE_NAME
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel

@Entity(tableName = TABLE_NAME)
data class ShortStockDbModel(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_SEC_ID)
    val secid: String,

    @ColumnInfo(name = COLUMN_NAME)
    val name: String
) {
    companion object {
        const val TABLE_NAME = "Stock"

        const val COLUMN_SEC_ID = "sec_id"
        const val COLUMN_NAME = "name"

        fun from(shortStock: ShortStockNetworkModel) = ShortStockDbModel(
            secid = shortStock.secid,
            name = shortStock.name
        )
    }
}