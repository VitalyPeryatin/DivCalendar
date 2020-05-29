package com.infinity_coder.divcalendar.data.db

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.infinity_coder.divcalendar.presentation.App

object DivCalendarDatabase {

    val roomDatabase: AppDatabase by lazy {
        Room.databaseBuilder(App.instance.applicationContext, AppDatabase::class.java, "div-calendar-database")
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE Post")
        }
    }
}