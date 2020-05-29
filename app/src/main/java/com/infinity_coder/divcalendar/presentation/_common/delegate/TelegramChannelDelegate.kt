package com.infinity_coder.divcalendar.presentation._common.delegate

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.infinity_coder.divcalendar.presentation._common.extensions.isAppAvailable

object TelegramChannelDelegate {

    private const val TELEGRAM_GROUP_LINK = "https://t.me/joinchat/H2bVmxsrivVBQ0rqv0AWIg"
    private const val TELEGRAM_PACKAGE = "org.telegram.messenger"
    private const val TELEGRAM_X_PACKAGE = "org.thunderdog.challegram"

    fun getOpenTelegramChannelIntent(context: Context): Intent {
        val telegramIntent = Intent(Intent.ACTION_VIEW, Uri.parse(TELEGRAM_GROUP_LINK))

        if (context.isAppAvailable(TELEGRAM_PACKAGE)) {
            telegramIntent.setPackage(TELEGRAM_PACKAGE)
        } else if (context.isAppAvailable(TELEGRAM_X_PACKAGE)) {
            telegramIntent.setPackage(TELEGRAM_X_PACKAGE)
        }

        return telegramIntent
    }
}