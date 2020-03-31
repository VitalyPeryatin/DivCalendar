package com.infinity_coder.divcalendar.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinity_coder.divcalendar.data.db.model.SecPackageDbModel.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class SecPackageDbModel(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_SEC_ID)
    val secid: String,

    @ColumnInfo(name = COLUMN_NAME)
    val name: String,

    var count: Int = 0,

    var totalPrice: Float = 0f
) {
    companion object {
        const val TABLE_NAME = "StockPackage"

        const val COLUMN_SEC_ID = "sec_id"
        const val COLUMN_NAME = "name"
    }
}