package com.infinity_coder.divcalendar.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.infinity_coder.divcalendar.data.db.dao.PostDao
import com.infinity_coder.divcalendar.data.db.dao.StockDao
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.db.model.SecPackageDbModel

@Database(entities = [SecPackageDbModel::class, PostDbModel::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract val stockDao: StockDao

    abstract val postDao: PostDao
}