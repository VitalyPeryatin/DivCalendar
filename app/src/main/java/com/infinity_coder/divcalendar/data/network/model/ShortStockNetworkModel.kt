package com.infinity_coder.divcalendar.data.network.model

data class ShortStockNetworkModel(
    val secid: String,
    val name: String
) {
    companion object {
        const val SEC_ID_COL_NAME = "SECID"
        const val NAME_COL_NAME = "NAME"
    }
}