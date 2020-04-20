package com.infinity_coder.divcalendar.presentation._common.extensions

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.infinity_coder.divcalendar.R

fun View.shake(x: Float) {
    ObjectAnimator.ofFloat(this, "translationX", 0f, x, 0f, -x).apply {
        repeatCount = 5
        duration = 100
    }.start()
}

@SuppressLint("InflateParams")
fun Context.showSuccessfulToast(layoutInflater: LayoutInflater, message: Int) {
    val toastView = layoutInflater.inflate(R.layout.toast_successful, null).apply {
        findViewById<TextView>(R.id.messageTextView).setText(message)
    }
    Toast(this).apply {
        duration = Toast.LENGTH_SHORT
        view = toastView
    }.show()
}

fun Context.dpToPx(dp: Float) = (dp * resources.displayMetrics.density)