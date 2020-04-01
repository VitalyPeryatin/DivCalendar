package com.infinity_coder.divcalendar.presentation._common

import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.loadImg(imageUrl: String, placeholderRes: Int? = null) {
    Picasso.get().load(imageUrl).apply {
        placeholderRes?.let {
            placeholder(it)
        }
    }.fit().into(this)
}

fun View.shake(x: Float) {
    ObjectAnimator.ofFloat(this, "translationX", 0f, x, 0f, -x).apply {
        repeatCount = 5
        duration = 100
    }.start()
}