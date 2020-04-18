package com.infinity_coder.divcalendar.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinity_coder.divcalendar.data.db.model.PostDbModel.Companion.TABLE_NAME
import com.infinity_coder.divcalendar.data.network.model.PostNetworkModel

@Entity(tableName = TABLE_NAME)
data class PostDbModel(
    @ColumnInfo(name = COLUMN_TITLE)
    var title: String = "",

    @ColumnInfo(name = COLUMN_TEXT)
    var text: String = "",

    @ColumnInfo(name = COLUMN_LOGO)
    var logo: String = "",

    @ColumnInfo(name = COLUMN_SOURCE)
    var source: String = "",

    @ColumnInfo(name = COLUMN_DATE)
    var date: String = "",

    @ColumnInfo(name = COLUMN_LINK)
    var link: String = "",

    @ColumnInfo(name = COLUMN_TICKER)
    var ticker: String = ""
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var id: Long = 0

    companion object {
        const val TABLE_NAME = "Post"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_TEXT = "text"
        const val COLUMN_LOGO = "logo"
        const val COLUMN_SOURCE = "source"
        const val COLUMN_DATE = "date"
        const val COLUMN_LINK = "link"
        const val COLUMN_TICKER = "ticker"

        fun from(post: PostNetworkModel.Response) = PostDbModel(
            title = post.title,
            text = post.text,
            logo = post.logo,
            source = post.source,
            date = post.date,
            link = post.link,
            ticker = post.ticker
        )

        fun from(posts: List<PostNetworkModel.Response>) = posts.map { from(it) }
    }
}