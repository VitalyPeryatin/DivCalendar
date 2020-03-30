package com.infinity_coder.divcalendar.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinity_coder.divcalendar.data.db.model.PostDbModel.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class PostDbModel(
    @ColumnInfo(name = COLUMN_TITLE)
    var title: String = "",

    @ColumnInfo(name = COLUMN_AUTHOR)
    var author: String = "",

    @ColumnInfo(name = COLUMN_PAYLOAD)
    var payload: String = "",

    @ColumnInfo(name = COLUMN_DATE)
    var date: String = ""
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var id: Long = 0

    companion object {
        const val TABLE_NAME = "Post"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_PAYLOAD = "payload"
        const val COLUMN_DATE = "release_date"
    }
}