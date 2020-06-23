package com.infinity_coder.divcalendar.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infinity_coder.divcalendar.BuildConfig
import com.infinity_coder.divcalendar.presentation._common.delegate.AppThemeDelegate
import com.infinity_coder.divcalendar.presentation._common.log

class AppActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        log("${activity::class.java}: onActivityCreated()")
        if (!BuildConfig.DEBUG) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        log("${activity::class.java}: onActivityDestroyed()")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }
}