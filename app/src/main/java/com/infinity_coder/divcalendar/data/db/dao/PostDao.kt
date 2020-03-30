package com.infinity_coder.divcalendar.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infinity_coder.divcalendar.data.db.model.PostDbModel

@Dao
abstract class PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPosts(posts: List<PostDbModel>)

    @Query("DELETE FROM ${PostDbModel.TABLE_NAME}")
    abstract suspend fun deleteAll()

    @Query("SELECT * FROM ${PostDbModel.TABLE_NAME}")
    abstract suspend fun getPosts(): List<PostDbModel>
}