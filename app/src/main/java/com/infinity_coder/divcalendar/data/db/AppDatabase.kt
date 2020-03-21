package com.infinity_coder.divcalendar.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.infinity_coder.divcalendar.data.db.dao.StockDao
import com.infinity_coder.divcalendar.data.db.model.StockPackageDbModel

@Database(entities = [StockPackageDbModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val stockDao: StockDao
}