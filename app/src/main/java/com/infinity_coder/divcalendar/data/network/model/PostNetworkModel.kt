package com.infinity_coder.divcalendar.data.network.model

import com.google.gson.annotations.SerializedName

class PostNetworkModel {

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

        @SerializedName("poster")
        var logo: String = "",

        @SerializedName("source")
        var source: String = "",

        @SerializedName("date")
        var date: String = "",

        @SerializedName("link")
        var link: String = ""
    )
}