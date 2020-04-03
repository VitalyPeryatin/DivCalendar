package com.infinity_coder.divcalendar.presentation._common

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.infinity_coder.divcalendar.R

object SimpleGlide {

    fun loadImage(view: View, url: String, target: ImageView) {
        if(isFormatImageSvg(url)) {
            Log.d("SvgFormat",url)
            loadSVG(view.context, url, target)
        }else{
            loadNotSVG(view.context, url, target)
        }
    }

    private fun isFormatImageSvg(url:String):Boolean{
        return url.split(".").run {
            isNotEmpty() && last() == "svg"
        }
    }

    private fun loadSVG(context: Context, url: String, target: ImageView) {
        GlideToVectorYou().apply {
            with(context)
            setPlaceHolder(R.drawable.logo_placeholder, R.drawable.logo_placeholder)
        }.load(Uri.parse(url), target)
    }

    private fun loadNotSVG(context: Context,url: String,target: ImageView){
        GlideApp.with(context)
            .load(url)
            .placeholder(R.drawable.logo_placeholder)
            .into(target)
    }

}