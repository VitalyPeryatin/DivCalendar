package com.infinity_coder.divcalendar.presentation._common

import android.animation.ObjectAnimator
import android.view.View

fun View.shake(x: Float) {
    ObjectAnimator.ofFloat(this, "translationX", 0f, x, 0f, -x).apply {
        repeatCount = 5
        duration = 100
    }.start()
}