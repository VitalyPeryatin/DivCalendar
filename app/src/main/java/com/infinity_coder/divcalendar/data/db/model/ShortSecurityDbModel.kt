package com.infinity_coder.divcalendar.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinity_coder.divcalendar.data.db.model.ShortSecurityDbModel.Companion.TABLE_NAME
import com.infinity_coder.divcalendar.data.network.model.ShortSecNetworkModel

@Entity(tableName = TABLE_NAME)
data class ShortSecurityDbModel(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_SEC_ID)
    val secid: String,

    @ColumnInfo(name = COLUMN_NAME)
    val name: String
) {
    companion object {
        const val TABLE_NAME = "Security"

        const val COLUMN_SEC_ID = "sec_id"
        const val COLUMN_NAME = "name"

        fun from(shortSecurity: ShortSecNetworkModel) = ShortSecurityDbModel(
            secid = shortSecurity.secid,
            name = shortSecurity.name
        )
    }
}