package com.infinity_coder.divcalendar.presentation._common

import android.widget.Toast
import com.infinity_coder.divcalendar.AppConfig
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.domain._common.ClickCounter
import com.infinity_coder.divcalendar.presentation.App

object SwitchServerVersionDelegate {

    private const val CLICK_THRESHOLD = 5
    private const val MAX_CLICK_AWAIT = 1000L

    private val clickCounter = ClickCounter(CLICK_THRESHOLD, MAX_CLICK_AWAIT)

    init {
        clickCounter.addSubscriber(this::switchAppConfig)
    }

    fun click() {
        clickCounter.click()
    }

    private fun switchAppConfig() {
        AppConfig.serverConfig = when (AppConfig.serverConfig) {
            AppConfig.PROD -> AppConfig.DEV
            else -> AppConfig.PROD
        }
        RetrofitService.initApi()
        showToast()
    }

    private fun showToast() {
        val message = when (AppConfig.serverConfig) {
            AppConfig.DEV -> "Установлен Dev сервер"
            AppConfig.PROD -> "Установлен Prod сервер"
            else -> "Произошла ошибка при переключении Dev/Prod серверов"
        }
        Toast.makeText(App.context, message, Toast.LENGTH_SHORT).show()
    }
}