package com.infinity_coder.divcalendar.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.infinity_coder.divcalendar.data.db.dao.PaymentDao
import com.infinity_coder.divcalendar.data.db.dao.PortfolioDao
import com.infinity_coder.divcalendar.data.db.dao.SecurityDao
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.db.typeconverter.BigDecimaltypeConverter

@Database(entities = [SecurityDbModel::class, PortfolioDbModel::class, PaymentDbModel::class], version = 5)
@TypeConverters(BigDecimaltypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val portfolioDao: PortfolioDao

    abstract val securityDao: SecurityDao

    abstract val paymentDao: PaymentDao
}