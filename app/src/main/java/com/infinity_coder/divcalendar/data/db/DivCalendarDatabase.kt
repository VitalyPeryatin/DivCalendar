package com.infinity_coder.divcalendar.data.db

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

            database.execSQL("CREATE TABLE ${SecurityDbModel.TABLE_NAME} ")

            database.execSQL("ALTER TABLE ${SecurityDbModel.TABLE_NAME} ADD COLUMN ${SecurityDbModel.COLUMN_MARKET} TEXT NOT NULL DEFAULT ''")
        }
    }
}