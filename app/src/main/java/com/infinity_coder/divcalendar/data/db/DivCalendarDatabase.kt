package com.infinity_coder.divcalendar.data.db

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
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

            database.execSQL("CREATE TABLE `${SecurityDbModel.TABLE_NAME}_copy` (" +
                    "${SecurityDbModel.COLUMN_ISIN} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_TICKER} TEXT NOT NULL, " +
                    "${SecurityDbModel.TABLE_NAME} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_TYPE} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_LOGO} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_YEAR_YIELD} REAL NOT NULL, " +
                    "${SecurityDbModel.COLUMN_EXCHANGE} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_CURRENCY} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_COUNT} INTEGER NOT NULL, " +
                    "${SecurityDbModel.COLUMN_TOTAL_PRICE} REAL NOT NULL, " +
                    "${SecurityDbModel.COLUMN_PORTFOLIO_ID} INTEGER NOT NULL, " +
                    "${SecurityDbModel.COLUMN_COLOR} INTEGER NOT NULL, " +
                    "${SecurityDbModel.COLUMN_MARKET} TEXT NOT NULL," +
                    "PRIMARY KEY (${SecurityDbModel.COLUMN_ISIN}, ${SecurityDbModel.COLUMN_PORTFOLIO_ID}, ${SecurityDbModel.COLUMN_EXCHANGE}, " +
                    "FOREIGN KEY (${SecurityDbModel.COLUMN_PORTFOLIO_ID}) ${PortfolioDbModel.TABLE_NAME} (${PortfolioDbModel.COLUMN_ID} ON DELETE CASCADE)"
            )

            database.execSQL("INSERT INTO `${SecurityDbModel.TABLE_NAME}_copy` (" +
                    "${SecurityDbModel.COLUMN_ISIN}, " +
                    "${SecurityDbModel.COLUMN_TICKER}, " +
                    "${SecurityDbModel.TABLE_NAME}, " +
                    "${SecurityDbModel.COLUMN_TYPE}, " +
                    "${SecurityDbModel.COLUMN_LOGO}, " +
                    "${SecurityDbModel.COLUMN_YEAR_YIELD}, " +
                    "${SecurityDbModel.COLUMN_EXCHANGE}, " +
                    "${SecurityDbModel.COLUMN_CURRENCY}, " +
                    "${SecurityDbModel.COLUMN_COUNT}, " +
                    "${SecurityDbModel.COLUMN_TOTAL_PRICE}, " +
                    "${SecurityDbModel.COLUMN_PORTFOLIO_ID}, " +
                    "${SecurityDbModel.COLUMN_COLOR}, " +
                    "${SecurityDbModel.COLUMN_MARKET}) " +
                    "SELECT " +
                    "${SecurityDbModel.COLUMN_ISIN}, " +
                    "${SecurityDbModel.COLUMN_TICKER}, " +
                    "${SecurityDbModel.TABLE_NAME}, " +
                    "${SecurityDbModel.COLUMN_TYPE}, " +
                    "${SecurityDbModel.COLUMN_LOGO}, " +
                    "${SecurityDbModel.COLUMN_YEAR_YIELD}, " +
                    "${SecurityDbModel.COLUMN_EXCHANGE}, " +
                    "${SecurityDbModel.COLUMN_CURRENCY}, " +
                    "${SecurityDbModel.COLUMN_COUNT}, " +
                    "${SecurityDbModel.COLUMN_TOTAL_PRICE}, " +
                    "${SecurityDbModel.COLUMN_PORTFOLIO_ID}, " +
                    "${SecurityDbModel.COLUMN_COLOR}, " +
                    "${SecurityDbModel.COLUMN_MARKET} FROM ${SecurityDbModel.TABLE_NAME}"
            )

            database.execSQL("DROP TABLE ${SecurityDbModel.TABLE_NAME}")
            database.execSQL("ALTER TABLE `${SecurityDbModel.TABLE_NAME}_copy` RENAME TO ${SecurityDbModel.TABLE_NAME}")

            database.execSQL("INSERT INTO `${SecurityDbModel.TABLE_NAME}_copy` (`isin`, `ticker`, `name`) SELECT `isin`, `ticker`, `name` FROM ${SecurityDbModel.TABLE_NAME}")
            database.execSQL("ALTER TABLE ${SecurityDbModel.TABLE_NAME} ADD COLUMN ${SecurityDbModel.COLUMN_MARKET} TEXT NOT NULL DEFAULT ''")
        }
    }
}