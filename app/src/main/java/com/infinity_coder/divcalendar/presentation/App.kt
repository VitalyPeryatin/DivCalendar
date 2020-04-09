package com.infinity_coder.divcalendar.presentation

import android.app.Application
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import com.infinity_coder.divcalendar.domain._common.Actualizer

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        instance = this
        registerActivityLifecycleCallbacks(AppActivityLifecycleCallbacks())
        initActualizer()
    }

    private fun initActualizer(){
        Actualizer.subscribe(RateRepository::updateRates, RATE_OUT_DATE_LIMIT)
    }

    companion object {
        private const val RATE_OUT_DATE_LIMIT = 30 * 60 * 1000L

        lateinit var instance: App
            private set
    }
}