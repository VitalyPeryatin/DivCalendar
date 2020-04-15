package com.infinity_coder.divcalendar.presentation._common

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.infinity_coder.divcalendar.BuildConfig

fun logException(obj: Any, throwable: Throwable?) {
    Log.w(obj.javaClass.simpleName, throwable)
    if (!BuildConfig.DEBUG && throwable != null) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}