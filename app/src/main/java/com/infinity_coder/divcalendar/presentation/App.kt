package com.infinity_coder.divcalendar.presentation

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        instance = this
        registerActivityLifecycleCallbacks(AppActivityLifecycleCallbacks())
    }

    companion object {
        lateinit var instance: App
            private set
    }
}