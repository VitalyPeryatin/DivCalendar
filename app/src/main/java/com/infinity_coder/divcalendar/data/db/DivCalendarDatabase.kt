package com.infinity_coder.divcalendar.data.db

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.presentation.App

object DivCalendarDatabase {

    val roomDatabase: AppDatabase by lazy {
        Room.databaseBuilder(App.instance.applicationContext, AppDatabase::class.java, "div-calendar-database")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)
            .build()
    }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE Post")
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${SecurityDbModel.TABLE_NAME} ADD COLUMN ${SecurityDbModel.COLUMN_MARKET} TEXT NOT NULL DEFAULT ''")
        }
    }

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.migrateSecurityTable()
            database.migratePaymentTable()
        }

        private fun SupportSQLiteDatabase.migrateSecurityTable() {
            execSQL("CREATE TABLE ${SecurityDbModel.TABLE_NAME}_copy (" +
                    "${SecurityDbModel.COLUMN_ISIN} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_TICKER} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_NAME} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_TYPE} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_LOGO} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_YEAR_YIELD} REAL NOT NULL, " +
                    "${SecurityDbModel.COLUMN_EXCHANGE} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_CURRENCY} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_COUNT} INTEGER NOT NULL, " +
                    "${SecurityDbModel.COLUMN_TOTAL_PRICE} REAL NOT NULL, " +
                    "${SecurityDbModel.COLUMN_PORTFOLIO_ID} INTEGER NOT NULL, " +
                    "${SecurityDbModel.COLUMN_COLOR} INTEGER NOT NULL, " +
                    "${SecurityDbModel.COLUMN_MARKET} TEXT NOT NULL, " +
                    "PRIMARY KEY (${SecurityDbModel.COLUMN_ISIN}, ${SecurityDbModel.COLUMN_PORTFOLIO_ID}, ${SecurityDbModel.COLUMN_EXCHANGE}), " +
                    "FOREIGN KEY (${SecurityDbModel.COLUMN_PORTFOLIO_ID}) REFERENCES ${PortfolioDbModel.TABLE_NAME} (${PortfolioDbModel.COLUMN_ID}) ON DELETE CASCADE)"
            )
            execSQL("INSERT INTO ${SecurityDbModel.TABLE_NAME}_copy (" +
                    "${SecurityDbModel.COLUMN_ISIN}, " +
                    "${SecurityDbModel.COLUMN_TICKER}, " +
                    "${SecurityDbModel.COLUMN_NAME}, " +
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
                    "${SecurityDbModel.COLUMN_NAME}, " +
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
            execSQL("DROP TABLE ${SecurityDbModel.TABLE_NAME}")
            execSQL("ALTER TABLE ${SecurityDbModel.TABLE_NAME}_copy RENAME TO ${SecurityDbModel.TABLE_NAME}")
            execSQL("CREATE INDEX IF NOT EXISTS ${SecurityDbModel.INDEX_PORTFOLIO_ID} ON ${SecurityDbModel.TABLE_NAME} (${PaymentDbModel.COLUMN_PORTFOLIO_ID})")
        }

        private fun SupportSQLiteDatabase.migratePaymentTable() {
            execSQL("CREATE TABLE ${PaymentDbModel.TABLE_NAME}_copy (" +
                    "${PaymentDbModel.COLUMN_DIVIDENDS} REAL NOT NULL, " +
                    "${PaymentDbModel.COLUMN_DATE} TEXT NOT NULL, " +
                    "${PaymentDbModel.COLUMN_FORECAST} INTEGER NOT NULL, " +
                    "${PaymentDbModel.COLUMN_ISIN} TEXT NOT NULL, " +
                    "${PaymentDbModel.COLUMN_PORTFOLIO_ID} INTEGER NOT NULL, " +
                    "${PaymentDbModel.COLUMN_EXCHANGE} TEXT NOT NULL DEFAULT '', " +
                    "${PaymentDbModel.COLUMN_COUNT} INTEGER, " +
                    "PRIMARY KEY (${PaymentDbModel.COLUMN_DATE}, ${PaymentDbModel.COLUMN_ISIN}, ${PaymentDbModel.COLUMN_PORTFOLIO_ID}, ${PaymentDbModel.COLUMN_EXCHANGE}), " +
                    "FOREIGN KEY (${PaymentDbModel.COLUMN_ISIN}, ${PaymentDbModel.COLUMN_PORTFOLIO_ID}, ${PaymentDbModel.COLUMN_EXCHANGE}) " +
                    "REFERENCES ${SecurityDbModel.TABLE_NAME} (${SecurityDbModel.COLUMN_ISIN}, ${SecurityDbModel.COLUMN_PORTFOLIO_ID}, ${SecurityDbModel.COLUMN_EXCHANGE}) " +
                    "ON DELETE CASCADE)"
            )
            execSQL("INSERT INTO ${PaymentDbModel.TABLE_NAME}_copy (" +
                    "${PaymentDbModel.COLUMN_DIVIDENDS}, " +
                    "${PaymentDbModel.COLUMN_DATE}, " +
                    "${PaymentDbModel.COLUMN_FORECAST}, " +
                    "${PaymentDbModel.COLUMN_ISIN}, " +
                    "${PaymentDbModel.COLUMN_PORTFOLIO_ID}) " +
                    "SELECT " +
                    "${PaymentDbModel.COLUMN_DIVIDENDS}, " +
                    "${PaymentDbModel.COLUMN_DATE}, " +
                    "${PaymentDbModel.COLUMN_FORECAST}, " +
                    "${PaymentDbModel.COLUMN_ISIN}, " +
                    "${PaymentDbModel.COLUMN_PORTFOLIO_ID} FROM ${PaymentDbModel.TABLE_NAME} "
            )
            execSQL("DROP TABLE ${PaymentDbModel.TABLE_NAME}")
            execSQL("ALTER TABLE ${PaymentDbModel.TABLE_NAME}_copy RENAME TO ${PaymentDbModel.TABLE_NAME}")
            execSQL("CREATE INDEX IF NOT EXISTS ${PaymentDbModel.INDEX_SECURITY} ON ${PaymentDbModel.TABLE_NAME} " +
                    "(${PaymentDbModel.COLUMN_ISIN}, ${PaymentDbModel.COLUMN_PORTFOLIO_ID}, ${PaymentDbModel.COLUMN_EXCHANGE})"
            )
        }
    }

    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DELETE FROM ${PaymentDbModel.TABLE_NAME} WHERE ${PaymentDbModel.COLUMN_EXCHANGE}='' AND EXISTS " +
                    "(SELECT ${PaymentDbModel.COLUMN_ISIN}, ${PaymentDbModel.COLUMN_PORTFOLIO_ID}, ${PaymentDbModel.COLUMN_DATE} FROM ${PaymentDbModel.TABLE_NAME} PaymentDuplicate " +
                    "WHERE ${PaymentDbModel.TABLE_NAME}.${PaymentDbModel.COLUMN_ISIN}=PaymentDuplicate.${PaymentDbModel.COLUMN_ISIN} " +
                    "AND ${PaymentDbModel.TABLE_NAME}.${PaymentDbModel.COLUMN_PORTFOLIO_ID}=PaymentDuplicate.${PaymentDbModel.COLUMN_PORTFOLIO_ID} " +
                    "AND ${PaymentDbModel.TABLE_NAME}.${PaymentDbModel.COLUMN_DATE}=PaymentDuplicate.${PaymentDbModel.COLUMN_DATE} " +
                    "AND PaymentDuplicate.${PaymentDbModel.COLUMN_EXCHANGE} <> '')"
            )
        }
    }

    private val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.migrateSecurityTable()
            database.migratePaymentTable()
        }

        private fun SupportSQLiteDatabase.migrateSecurityTable() {
            execSQL("CREATE TABLE ${SecurityDbModel.TABLE_NAME}_copy (" +
                    "${SecurityDbModel.COLUMN_ISIN} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_TICKER} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_NAME} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_TYPE} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_LOGO} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_YEAR_YIELD} REAL NOT NULL, " +
                    "${SecurityDbModel.COLUMN_EXCHANGE} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_CURRENCY} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_COUNT} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_TOTAL_PRICE} TEXT NOT NULL, " +
                    "${SecurityDbModel.COLUMN_PORTFOLIO_ID} INTEGER NOT NULL, " +
                    "${SecurityDbModel.COLUMN_COLOR} INTEGER NOT NULL, " +
                    "${SecurityDbModel.COLUMN_MARKET} TEXT NOT NULL, " +
                    "PRIMARY KEY (${SecurityDbModel.COLUMN_ISIN}, ${SecurityDbModel.COLUMN_PORTFOLIO_ID}, ${SecurityDbModel.COLUMN_EXCHANGE}), " +
                    "FOREIGN KEY (${SecurityDbModel.COLUMN_PORTFOLIO_ID}) REFERENCES ${PortfolioDbModel.TABLE_NAME} (${PortfolioDbModel.COLUMN_ID}) ON DELETE CASCADE)"
            )
            execSQL("INSERT INTO ${SecurityDbModel.TABLE_NAME}_copy (" +
                    "${SecurityDbModel.COLUMN_ISIN}, " +
                    "${SecurityDbModel.COLUMN_TICKER}, " +
                    "${SecurityDbModel.COLUMN_NAME}, " +
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
                    "${SecurityDbModel.COLUMN_NAME}, " +
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
            execSQL("DROP TABLE ${SecurityDbModel.TABLE_NAME}")
            execSQL("ALTER TABLE ${SecurityDbModel.TABLE_NAME}_copy RENAME TO ${SecurityDbModel.TABLE_NAME}")
            execSQL("CREATE INDEX IF NOT EXISTS ${SecurityDbModel.INDEX_PORTFOLIO_ID} ON ${SecurityDbModel.TABLE_NAME} (${PaymentDbModel.COLUMN_PORTFOLIO_ID})")
        }

        private fun SupportSQLiteDatabase.migratePaymentTable() {
            execSQL("CREATE TABLE ${PaymentDbModel.TABLE_NAME}_copy (" +
                    "${PaymentDbModel.COLUMN_DIVIDENDS} TEXT NOT NULL, " +
                    "${PaymentDbModel.COLUMN_DATE} TEXT NOT NULL, " +
                    "${PaymentDbModel.COLUMN_FORECAST} INTEGER NOT NULL, " +
                    "${PaymentDbModel.COLUMN_ISIN} TEXT NOT NULL, " +
                    "${PaymentDbModel.COLUMN_PORTFOLIO_ID} INTEGER NOT NULL, " +
                    "${PaymentDbModel.COLUMN_EXCHANGE} TEXT NOT NULL DEFAULT '', " +
                    "${PaymentDbModel.COLUMN_COUNT} TEXT, " +
                    "PRIMARY KEY (${PaymentDbModel.COLUMN_DATE}, ${PaymentDbModel.COLUMN_ISIN}, ${PaymentDbModel.COLUMN_PORTFOLIO_ID}, ${PaymentDbModel.COLUMN_EXCHANGE}), " +
                    "FOREIGN KEY (${PaymentDbModel.COLUMN_ISIN}, ${PaymentDbModel.COLUMN_PORTFOLIO_ID}, ${PaymentDbModel.COLUMN_EXCHANGE}) " +
                    "REFERENCES ${SecurityDbModel.TABLE_NAME} (${SecurityDbModel.COLUMN_ISIN}, ${SecurityDbModel.COLUMN_PORTFOLIO_ID}, ${SecurityDbModel.COLUMN_EXCHANGE}) " +
                    "ON DELETE CASCADE)"
            )
            execSQL("INSERT INTO ${PaymentDbModel.TABLE_NAME}_copy (" +
                    "${PaymentDbModel.COLUMN_DIVIDENDS}, " +
                    "${PaymentDbModel.COLUMN_DATE}, " +
                    "${PaymentDbModel.COLUMN_FORECAST}, " +
                    "${PaymentDbModel.COLUMN_ISIN}, " +
                    "${PaymentDbModel.COLUMN_PORTFOLIO_ID}) " +
                    "SELECT " +
                    "${PaymentDbModel.COLUMN_DIVIDENDS}, " +
                    "${PaymentDbModel.COLUMN_DATE}, " +
                    "${PaymentDbModel.COLUMN_FORECAST}, " +
                    "${PaymentDbModel.COLUMN_ISIN}, " +
                    "${PaymentDbModel.COLUMN_PORTFOLIO_ID} FROM ${PaymentDbModel.TABLE_NAME} "
            )
            execSQL("UPDATE ${PaymentDbModel.TABLE_NAME}_copy SET ${PaymentDbModel.COLUMN_EXCHANGE}=(" +
                    "SELECT ${SecurityDbModel.TABLE_NAME}.${SecurityDbModel.COLUMN_EXCHANGE} " +
                    "FROM ${SecurityDbModel.TABLE_NAME} WHERE " +
                    "${SecurityDbModel.TABLE_NAME}.${SecurityDbModel.COLUMN_ISIN}=${PaymentDbModel.TABLE_NAME}_copy.${PaymentDbModel.COLUMN_ISIN} " +
                    "AND ${SecurityDbModel.TABLE_NAME}.${SecurityDbModel.COLUMN_PORTFOLIO_ID}=${PaymentDbModel.TABLE_NAME}_copy.${PaymentDbModel.COLUMN_PORTFOLIO_ID})"
            )
            execSQL("DROP TABLE ${PaymentDbModel.TABLE_NAME}")
            execSQL("ALTER TABLE ${PaymentDbModel.TABLE_NAME}_copy RENAME TO ${PaymentDbModel.TABLE_NAME}")
            execSQL("CREATE INDEX IF NOT EXISTS ${PaymentDbModel.INDEX_SECURITY} ON ${PaymentDbModel.TABLE_NAME} " +
                    "(${PaymentDbModel.COLUMN_ISIN}, ${PaymentDbModel.COLUMN_PORTFOLIO_ID}, ${PaymentDbModel.COLUMN_EXCHANGE})"
            )
        }
    }

    private val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.migratePaymentTable()
        }

        private fun SupportSQLiteDatabase.migratePaymentTable() {
            execSQL("CREATE TABLE ${PaymentDbModel.TABLE_NAME}_copy (" +
                    "${PaymentDbModel.COLUMN_DIVIDENDS} TEXT NOT NULL, " +
                    "${PaymentDbModel.COLUMN_DATE} TEXT NOT NULL, " +
                    "${PaymentDbModel.COLUMN_FORECAST} INTEGER NOT NULL, " +
                    "${PaymentDbModel.COLUMN_IS_SYNCED} INTEGER," +
                    "${PaymentDbModel.COLUMN_ISIN} TEXT NOT NULL, " +
                    "${PaymentDbModel.COLUMN_PORTFOLIO_ID} INTEGER NOT NULL, " +
                    "${PaymentDbModel.COLUMN_PAYMENT_UUID} INTEGER," +
                    "${PaymentDbModel.COLUMN_EXCHANGE} TEXT NOT NULL DEFAULT '', " +
                    "${PaymentDbModel.COLUMN_COUNT} TEXT, " +
                    "PRIMARY KEY (${PaymentDbModel.COLUMN_DATE}, ${PaymentDbModel.COLUMN_ISIN}, ${PaymentDbModel.COLUMN_PORTFOLIO_ID}, ${PaymentDbModel.COLUMN_EXCHANGE}, ${PaymentDbModel.COLUMN_PAYMENT_UUID}), " +
                    "FOREIGN KEY (${PaymentDbModel.COLUMN_ISIN}, ${PaymentDbModel.COLUMN_PORTFOLIO_ID}, ${PaymentDbModel.COLUMN_EXCHANGE}) " +
                    "REFERENCES ${SecurityDbModel.TABLE_NAME} (${SecurityDbModel.COLUMN_ISIN}, ${SecurityDbModel.COLUMN_PORTFOLIO_ID}, ${SecurityDbModel.COLUMN_EXCHANGE}) " +
                    "ON DELETE CASCADE)"
            )
            execSQL("INSERT INTO ${PaymentDbModel.TABLE_NAME}_copy (" +
                    "${PaymentDbModel.COLUMN_DIVIDENDS}, " +
                    "${PaymentDbModel.COLUMN_DATE}, " +
                    "${PaymentDbModel.COLUMN_FORECAST}, " +
                    "${PaymentDbModel.COLUMN_ISIN}, " +
                    "${PaymentDbModel.COLUMN_PORTFOLIO_ID}) " +
                    "SELECT " +
                    "${PaymentDbModel.COLUMN_DIVIDENDS}, " +
                    "${PaymentDbModel.COLUMN_DATE}, " +
                    "${PaymentDbModel.COLUMN_FORECAST}, " +
                    "${PaymentDbModel.COLUMN_ISIN}, " +
                    "${PaymentDbModel.COLUMN_PORTFOLIO_ID} FROM ${PaymentDbModel.TABLE_NAME} "
            )
            execSQL("DROP TABLE ${PaymentDbModel.TABLE_NAME}")
            execSQL("ALTER TABLE ${PaymentDbModel.TABLE_NAME}_copy RENAME TO ${PaymentDbModel.TABLE_NAME}")
            execSQL("CREATE INDEX IF NOT EXISTS ${PaymentDbModel.INDEX_SECURITY} ON ${PaymentDbModel.TABLE_NAME} " +
                    "(${PaymentDbModel.COLUMN_ISIN}, ${PaymentDbModel.COLUMN_PORTFOLIO_ID}, ${PaymentDbModel.COLUMN_EXCHANGE})")
        }
    }
}