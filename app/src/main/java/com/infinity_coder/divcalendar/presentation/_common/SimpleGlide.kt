package com.infinity_coder.divcalendar.presentation._common

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.infinity_coder.divcalendar.R

object SimpleGlide {
    fun loadSVG(context: Context?, url: String, target: ImageView) {
        GlideToVectorYou
            .init()
            .with(context)
            .setPlaceHolder(R.drawable.logo_placeholder, R.drawable.logo_placeholder)
            .load(Uri.parse(url), target)
    }

    fun loadSVG(view: View, url: String, target: ImageView) {
        loadSVG(view.context, url, target)
    }
}