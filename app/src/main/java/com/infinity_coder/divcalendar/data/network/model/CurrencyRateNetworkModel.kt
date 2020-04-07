package com.infinity_coder.divcalendar.data.network.model

import com.google.gson.annotations.SerializedName

data class CurrencyRateNetworkModel(
    @SerializedName("rubToUsd")
    val rubToUsd: Float,

    @SerializedName("usdToRub")
    val usdToRub: Float
)