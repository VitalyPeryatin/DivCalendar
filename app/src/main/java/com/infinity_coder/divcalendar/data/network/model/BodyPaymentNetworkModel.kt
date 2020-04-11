package com.infinity_coder.divcalendar.data.network.model

import com.google.gson.annotations.SerializedName

data class BodyPaymentNetworkModel(
    @SerializedName("securities")
    val securities:List<String>,

    @SerializedName("start_date")
    val startDate:String,

    @SerializedName("end_date")
    val endDate:String
)