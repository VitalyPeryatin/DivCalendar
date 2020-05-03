package com.infinity_coder.divcalendar.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.infinity_coder.divcalendar.data.db.dao.NewsPostDao
import com.infinity_coder.divcalendar.data.db.dao.PaymentDao
import com.infinity_coder.divcalendar.data.db.dao.PortfolioDao
import com.infinity_coder.divcalendar.data.db.dao.SecurityDao
import com.infinity_coder.divcalendar.data.db.model.NewsPostDbModel
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel

@Database(entities = [SecurityDbModel::class, NewsPostDbModel::class,
    PortfolioDbModel::class, PaymentDbModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val newsDao: NewsPostDao

    abstract val portfolioDao: PortfolioDao

    abstract val securityDao: SecurityDao

    abstract val paymentDao: PaymentDao
}