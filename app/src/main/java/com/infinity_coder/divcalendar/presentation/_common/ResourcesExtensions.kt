package com.infinity_coder.divcalendar.presentation._common

import android.content.res.Resources
import android.os.Build
import androidx.annotation.ColorRes

@Suppress("DEPRECATION")
fun Resources.getColorExt(@ColorRes colorId: Int, theme: Resources.Theme): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getColor(colorId, theme)
    } else {
        getColor(colorId)
    }
}