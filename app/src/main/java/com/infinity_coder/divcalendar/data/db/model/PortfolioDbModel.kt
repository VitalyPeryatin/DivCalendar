package com.infinity_coder.divcalendar.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel.Companion.COLUMN_NAME
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME, indices = [Index(value = [COLUMN_NAME], unique = true)])
data class PortfolioDbModel(
    @ColumnInfo(name = COLUMN_NAME)
    var name: String = ""
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var id: Long = 0

    companion object {
        const val TABLE_NAME = "Portfolio"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
    }
}