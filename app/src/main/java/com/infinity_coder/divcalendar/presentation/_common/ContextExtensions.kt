package com.infinity_coder.divcalendar.presentation._common

import android.content.Context
import android.os.Build
import androidx.annotation.ColorRes

@Suppress("DEPRECATION")
fun Context.getColorExt(@ColorRes colorId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(colorId, theme)
    } else {
        resources.getColor(colorId)
    }
}