package com.infinity_coder.divcalendar.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class PortfolioDbModel(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_NAME)
    var name: String = ""
) {
    companion object {
        const val TABLE_NAME = "Portfolio"

        const val COLUMN_NAME = "name"
    }
}