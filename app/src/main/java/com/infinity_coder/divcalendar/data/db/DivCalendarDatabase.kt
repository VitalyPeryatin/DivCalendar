package com.infinity_coder.divcalendar.data.db

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.infinity_coder.divcalendar.presentation.App

object DivCalendarDatabase {

    val roomDatabase: AppDatabase by lazy {
        Room.databaseBuilder(App.instance.applicationContext, AppDatabase::class.java, "div-calendar-database")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE Post")
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.apply {
                execSQL("CREATE TABLE NewPayment(dividends TEXT NOT NULL, date TEXT NOT NULL, forecast INTEGER NOT NULL, isin TEXT NOT NULL, portfolio_id INTEGER NOT NULL, count TEXT NOT NULL, PRIMARY KEY(date, isin, portfolio_id), FOREIGN KEY(isin, portfolio_id) REFERENCES Security(isin, portfolio_id) ON DELETE CASCADE)")
                execSQL("INSERT INTO NewPayment(dividends, date, forecast, isin, portfolio_id, count) SELECT dividends, date, forecast, isin, portfolio_id, count FROM Payment")
                execSQL("DROP TABLE Payment")
                execSQL("CREATE INDEX security_index ON NewPayment(isin, portfolio_id)")
                execSQL("ALTER TABLE NewPayment RENAME TO Payment")

                execSQL("CREATE TABLE NewSecurity (isin TEXT NOT NULL, ticker TEXT NOT NULL, name TEXT NOT NULL, type TEXT NOT NULL, logo TEXT NOT NULL, year_yield REAL NOT NULL, exchange TEXT, currency TEXT NOT NULL, count TEXT NOT NULL, total_price TEXT NOT NULL, portfolio_id INTEGER NOT NULL, color INTEGER NOT NULL, PRIMARY KEY(isin, portfolio_id), FOREIGN KEY(portfolio_id) REFERENCES Portfolio(id) ON DELETE CASCADE)")
                execSQL("INSERT INTO NewSecurity(isin, ticker, name, type, logo, year_yield, exchange, currency, count, total_price, portfolio_id, color) SELECT isin, ticker, name, type, logo, year_yield, exchange, currency, count, total_price, portfolio_id, color FROM Security")
                execSQL("DROP TABLE Security")
                execSQL("CREATE INDEX portfolio_id_index ON NewSecurity(portfolio_id)")
                execSQL("ALTER TABLE NewSecurity RENAME TO Security")
            }
        }
    }
}