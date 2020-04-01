package com.infinity_coder.divcalendar.data.network.model

import com.google.gson.annotations.SerializedName

data class PostNetworkModel(
    @SerializedName("title")
    var title: String = "",

    @SerializedName("text")
    var text: String = "",

    @SerializedName("logo")
    var logo: String = "",

    @SerializedName("ticker")
    var ticker: String = "",

    @SerializedName("date")
    var date: String = "",

    @SerializedName("link")
    var link: String = ""
)