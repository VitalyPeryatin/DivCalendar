package com.infinity_coder.divcalendar.data.network.model

import com.google.gson.annotations.SerializedName

class NewsPostNetModel {

    data class Request(
        @SerializedName("securities")
        val securities: List<String>,

        @SerializedName("limit")
        val limit: Int,

        @SerializedName("offset")
        val offset: Int
    )

    data class Response(
        @SerializedName("title")
        var title: String = "",

        @SerializedName("text")
        var text: String = "",

        @SerializedName("logo")
        var logo: String = "",

        @SerializedName("poster")
        var poster: String = "",

        @SerializedName("source")
        var source: String = "",

        @SerializedName("date")
        var date: String = "",

        @SerializedName("link")
        var link: String = "",

        @SerializedName("ticker")
        var ticker: String = ""
    )
}