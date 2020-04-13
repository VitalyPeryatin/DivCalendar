package com.infinity_coder.divcalendar.data.network.model

import com.google.gson.annotations.SerializedName

class PaymentNetworkModel {

    data class Request(
        @SerializedName("securities")
        val securities: List<String>,

        @SerializedName("start_date")
        val startDate: String,

        @SerializedName("end_date")
        val endDate: String
    )

    data class Response(
        @SerializedName("dividends")
        val dividends: Double,

        @SerializedName("date")
        val date: String,

        @SerializedName("logo")
        val logo: String,

        @SerializedName("forecast")
        val forecast: String,

        @SerializedName("name")
        val ticker: String,

        @SerializedName("currency")
        val currency: String
    )
}