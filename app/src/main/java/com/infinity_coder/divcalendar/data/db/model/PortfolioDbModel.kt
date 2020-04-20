package com.infinity_coder.divcalendar.data.db.model

import androidx.room.*
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

    @Ignore
    var securities: List<SecurityDbModel> = emptyList()

    companion object {
        const val TABLE_NAME = "Portfolio"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
    }
}