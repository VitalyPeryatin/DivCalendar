package com.infinity_coder.divcalendar.presentation._common.extensions

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.infinity_coder.divcalendar.R
import kotlinx.android.synthetic.main.toast_successful.view.*

fun View.shake(x: Float) {
    ObjectAnimator.ofFloat(this, "translationX", 0f, x, 0f, -x).apply {
        repeatCount = 5
        duration = 100
    }.start()
}

@SuppressLint("InflateParams")
fun Context.showSuccessfulToast(layoutInflater: LayoutInflater, message: Int) {
    val toastView = layoutInflater.inflate(R.layout.toast_successful, null)
    toastView.messageTextView.setText(message)
    Toast(this).apply {
        duration = Toast.LENGTH_SHORT
        setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        view = toastView
    }.show()
}

fun Context.dpToPx(dp: Float) = (dp * resources.displayMetrics.density)