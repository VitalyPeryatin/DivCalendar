package com.infinity_coder.divcalendar.data.network.model

import com.google.gson.annotations.SerializedName

data class PaymentNetworkModel(
    @SerializedName("dividends")
    val dividends:Double,

    @SerializedName("date")
    val date:String,

    @SerializedName("logo")
    val logo:String,

    @SerializedName("forecast")
    val forecast:String,

    @SerializedName("name")
    val ticker:String,

    @SerializedName("currency")
    val currency:String
)