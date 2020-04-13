package com.infinity_coder.divcalendar.data.network.model

import com.google.gson.annotations.SerializedName

data class PaymentNetworkModel(
    @SerializedName("name")
    val name: String,

    @SerializedName("logo")
    val logo: String,

    @SerializedName("count")
    val count: Int,

    @SerializedName("dividends")
    var dividends: Double,

    @SerializedName("date")
    val date: String,

    @SerializedName("currency")
    val currency: String
)