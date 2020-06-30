package com.infinity_coder.divcalendar.presentation._common

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.infinity_coder.divcalendar.BuildConfig
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation.App
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
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

fun md5(str: String): String {
    return try {
        val messageDigest = MessageDigest.getInstance("MD5").run {
            update(str.toByteArray())
            digest()
        }

        StringBuilder().apply {
            for (i in messageDigest.indices) {
                append(Integer.toHexString(0xFF and messageDigest[i].toInt()))
            }
        }.toString()
    } catch (e: NoSuchAlgorithmException) {
        ""
    }
}

fun AppCompatActivity.setActionBar(toolbar: Toolbar, hasBackNavigation: Boolean = false, isShowTitle: Boolean = true) {
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(hasBackNavigation)
    supportActionBar?.setDisplayShowTitleEnabled(isShowTitle)
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
    toolbar.setNavigationOnClickListener { onBackPressed() }
}

fun Fragment.setActionBar(toolbar: Toolbar, hasBackNavigation: Boolean = false, isShowTitle: Boolean = true) {
    (activity as? AppCompatActivity)?.setActionBar(toolbar, hasBackNavigation, isShowTitle)
}