package com.infinity_coder.divcalendar.presentation._common

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.infinity_coder.divcalendar.BuildConfig
import com.infinity_coder.divcalendar.presentation.App
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

val logFile = File(App.instance.filesDir, "log.txt")

@SuppressLint("ConstantLocale")
private val dateFormatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SS", Locale.getDefault())

fun logException(obj: Any, throwable: Throwable?) {
    Log.w(obj.javaClass.simpleName, throwable)
    log(throwable?.message.toString())
    if (!BuildConfig.DEBUG && throwable != null) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}

fun log(text: String) {

    if (!logFile.exists()) { logFile.createNewFile() }

    BufferedWriter(FileWriter(logFile, true)).use {
        it.append(dateFormatter.format(Date()))
        it.newLine()
        it.append(text)
        it.newLine()
    }
}

fun clearLogFile() {
    if (!logFile.exists()) { logFile.createNewFile() }

    logFile.printWriter().use {
        it.print("")
    }
}