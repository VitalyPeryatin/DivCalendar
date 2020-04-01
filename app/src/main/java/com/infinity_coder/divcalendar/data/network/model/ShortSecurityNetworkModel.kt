package com.infinity_coder.divcalendar.data.network.model

import com.infinity_coder.divcalendar.presentation._common.SecurityTypeDelegate

data class ShortSecurityNetworkModel(
    val secid: String,
    val name: String,
    val type: String = SecurityTypeDelegate.SEC_TYP_STOCK
)