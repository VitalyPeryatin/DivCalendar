package com.infinity_coder.divcalendar.data.db

import androidx.room.Room
import com.infinity_coder.divcalendar.presentation.App

object DivCalendarDatabase {
    val roomDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            App.instance.applicationContext,
            AppDatabase::class.java,
            "div-calendar-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}