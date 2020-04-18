package com.infinity_coder.divcalendar.data.network.model

import com.google.gson.annotations.SerializedName

data class SecurityNetworkModel(
    @SerializedName("ticker")
    val ticker: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("logo")
    val logo: String = "",

    @SerializedName("yield")
    val yearYield: Float = 0f,

    @SerializedName("exchange")
    val exchange: String = "",

    @SerializedName("currency")
    val currency: String = ""
)