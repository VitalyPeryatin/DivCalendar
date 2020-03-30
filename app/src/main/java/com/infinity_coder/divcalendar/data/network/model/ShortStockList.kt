package com.infinity_coder.divcalendar.data.network.model

data class ShortStockList(
    val stocks: MutableList<ShortStockNetworkModel> = mutableListOf()
)