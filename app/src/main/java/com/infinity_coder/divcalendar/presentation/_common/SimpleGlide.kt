package com.infinity_coder.divcalendar.presentation._common

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYouListener
import com.infinity_coder.divcalendar.R

object SimpleGlide {

    fun loadImage(
        view: View,
        url: String,
        target: ImageView,
        error: Int = R.drawable.ic_logo_error
    ) {
        if (isFormatImageSvg(url)) {
            loadSVG(view.context, url, target, error)
        } else {
            loadNotSVG(view.context, url, target, error)
        }
    }

    private fun isFormatImageSvg(url: String): Boolean {
        return url.split(".").run {
            isNotEmpty() && last() == "svg"
        }
    }

    private fun loadSVG(context: Context, url: String, target: ImageView, error: Int) {
        GlideToVectorYou().apply {
            with(context)
            setPlaceHolder(R.color.logo_placeholder, R.color.logo_placeholder)
            withListener(object : GlideToVectorYouListener {
                override fun onLoadFailed() {
                    target.setImageResource(error)
                }

                override fun onResourceReady() {
                }
            })
        }.load(Uri.parse(url), target)
    }

    private fun loadNotSVG(context: Context, url: String, target: ImageView, error: Int) {
        GlideApp.with(context)
            .load(url)
            .placeholder(R.color.logo_placeholder)
            .error(error)
            .into(target)
    }

}