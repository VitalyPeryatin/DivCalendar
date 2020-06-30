package com.infinity_coder.divcalendar.presentation.browser

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.CustomTabHelper
import com.infinity_coder.divcalendar.presentation._common.delegate.AppThemeDelegate
import com.infinity_coder.divcalendar.presentation._common.setActionBar
import kotlinx.android.synthetic.main.activity_browser.*

class BrowserActivity : AppCompatActivity() {

    companion object {

        private const val URL_KEY = "url"

        fun openActivity(context: Context, url: String) {

            val packageNameToUse = CustomTabHelper.getPackageNameToUse(context)
            if (packageNameToUse != null) {
                val uri = Uri.parse(url)
                val toolbarColor = ContextCompat.getColor(context, R.color.background)

                val customTabsIntent = CustomTabsIntent.Builder()
                        .setToolbarColor(toolbarColor)
                        .build()

                customTabsIntent.intent.`package` = packageNameToUse
                customTabsIntent.launchUrl(context, uri)
            } else {
                val intent = Intent(context, BrowserActivity::class.java)
                val bundle = Bundle().apply {
                    putString(URL_KEY, url)
                }
                intent.putExtras(bundle)
                context.startActivity(intent)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        AppThemeDelegate.setAppTheme(this)

        super.onCreate(savedInstanceState)

        val bundle = intent.extras!!
        val url = bundle.getString(URL_KEY)

        setContentView(R.layout.activity_browser)

        setActionBar(browserToolbar, hasBackNavigation = true)
        browserToolbar?.title = url

        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = AppWebViewClient()
        webView.loadUrl(url)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home -> onBackPressed()

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private class AppWebViewClient : WebViewClient() {
        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            view.loadUrl(request.url.toString())
            return true
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}