package com.infinity_coder.divcalendar.presentation._common

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

object CustomTabHelper {

    private const val ACTION_CUSTOM_TABS_CONNECTION =
        "android.support.customtabs.action.CustomTabsService"

    private const val STABLE_PACKAGE = "com.android.chrome"
    private const val BETA_PACKAGE = "com.chrome.beta"
    private const val DEV_PACKAGE = "com.chrome.dev"
    private const val LOCAL_PACKAGE = "com.google.android.apps.chrome"

    private var isGetPackageName = false
    private var packageNameToUse: String? = null

    fun getPackageNameToUse(context: Context): String? {

        if (isGetPackageName) return packageNameToUse

        val packageManager = context.packageManager

        val defaultPackageName = getDefaultPackageName(packageManager)
        val packagesSupportingCustomTabs = getPackagesSupportingCustomTabs(packageManager)

        when {
            packagesSupportingCustomTabs.isEmpty() -> {
                packageNameToUse = null
            }
            packagesSupportingCustomTabs.size == 1 -> {
                packageNameToUse = packagesSupportingCustomTabs[0]
            }
            defaultPackageName != null &&
                !hasSpecializedHandlerIntents(packageManager)
                && packagesSupportingCustomTabs.contains(defaultPackageName) -> {
                packageNameToUse = defaultPackageName
            }
            packagesSupportingCustomTabs.contains(STABLE_PACKAGE) -> {
                packageNameToUse = STABLE_PACKAGE
            }
            packagesSupportingCustomTabs.contains(BETA_PACKAGE) -> {
                packageNameToUse = BETA_PACKAGE
            }
            packagesSupportingCustomTabs.contains(DEV_PACKAGE) -> {
                packageNameToUse = DEV_PACKAGE
            }
            packagesSupportingCustomTabs.contains(LOCAL_PACKAGE) -> {
                packageNameToUse = LOCAL_PACKAGE
            }
        }

        isGetPackageName = true

        return packageNameToUse
    }

    private fun getDefaultPackageName(packageManager: PackageManager): String? {
        val linkIntent = getLinkIntent()
        val defaultViewHandlerInfo = packageManager.resolveActivity(linkIntent, 0)
        var defaultPackageName: String? = null
        if (defaultViewHandlerInfo != null) {
            defaultPackageName = defaultViewHandlerInfo.activityInfo.packageName
        }

        return defaultPackageName
    }

    private fun getPackagesSupportingCustomTabs(packageManager: PackageManager): List<String> {
        val linkIntent = getLinkIntent()
        val resolvedActivityList = packageManager.queryIntentActivities(linkIntent, 0)
        val packagesSupportingCustomTabs: MutableList<String> = ArrayList()

        for (info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)
            if (packageManager.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName)
            }
        }

        return packagesSupportingCustomTabs
    }

    private fun hasSpecializedHandlerIntents(packageManager: PackageManager): Boolean {
        val linkIntent = getLinkIntent()
        try {
            val handlers =
                packageManager.queryIntentActivities(linkIntent, PackageManager.GET_RESOLVED_FILTER)
            if (handlers.size == 0) {
                return false
            }
            for (resolveInfo in handlers) {
                val filter = resolveInfo.filter ?: continue
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue
                if (resolveInfo.activityInfo == null) continue
                return true
            }
        } catch (e: RuntimeException) {
            logException(this, e)
        }
        return false
    }

    private fun getLinkIntent(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
    }
}