package com.infinity_coder.divcalendar.presentation._common.extensions

import android.content.SharedPreferences

fun SharedPreferences.getNotNullString(key: String, defValue: String = ""): String =
    getString(key, defValue) ?: defValue