package com.infinity_coder.divcalendar.presentation._common.extensions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

fun AppCompatActivity.setActionBar(toolbar: Toolbar, hasBackNavigation: Boolean = false) {
    setSupportActionBar(toolbar)

    if (hasBackNavigation) {
        supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }
}

fun Activity.isAppAvailable(appName: String?): Boolean {
    return try {
        packageManager.getPackageInfo(appName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}