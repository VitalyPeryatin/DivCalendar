package com.infinity_coder.divcalendar.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.infinity_coder.divcalendar.data.db.dao.NewsDao
import com.infinity_coder.divcalendar.data.db.dao.PortfolioDao
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel

@Database(entities = [SecurityPackageDbModel::class, PostDbModel::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract val portfolioDao: PortfolioDao

    abstract val newsDao: NewsDao
}