package com.infinity_coder.divcalendar.presentation._common

import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso

fun View.visibilityGone(show:Boolean){
    visibility = if(show) View.VISIBLE else View.GONE
}

fun ImageView.loadImg(imageUrl: String, placeholderRes: Int? = null) {
    Picasso.get().load(imageUrl).apply {
        placeholderRes?.let { placeholder(it) }
        fit().into(this@loadImg)
    }
}