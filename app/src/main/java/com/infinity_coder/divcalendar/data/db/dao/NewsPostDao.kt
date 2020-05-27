package com.infinity_coder.divcalendar.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infinity_coder.divcalendar.data.db.model.NewsPostDbModel

@Dao
abstract class NewsPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPosts(posts: List<NewsPostDbModel>)

    @Query("DELETE FROM ${NewsPostDbModel.TABLE_NAME}")
    abstract suspend fun deleteAll()

    @Query("SELECT * FROM ${NewsPostDbModel.TABLE_NAME} WHERE ${NewsPostDbModel.COLUMN_TICKER} IN (:tickers)")
    abstract suspend fun getPosts(tickers: List<String>): List<NewsPostDbModel>

    @Query("SELECT * FROM ${NewsPostDbModel.TABLE_NAME}")
    abstract suspend fun getAllNews(): List<NewsPostDbModel>
}