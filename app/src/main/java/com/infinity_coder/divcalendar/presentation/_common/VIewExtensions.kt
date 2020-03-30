package com.infinity_coder.divcalendar.presentation._common

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.loadImg(imageUrl: String, placeholderRes: Int? = null) {
    Picasso.get().load(imageUrl).apply {
        placeholderRes?.let {
            placeholder(it)
        }
    }.fit().into(this)
}