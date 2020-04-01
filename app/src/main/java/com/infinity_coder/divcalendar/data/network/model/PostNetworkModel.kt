package com.infinity_coder.divcalendar.data.network.model

import com.google.gson.annotations.SerializedName

data class PostNetworkModel(
    @SerializedName("title")
    var title: String = "",

    @SerializedName("text")
    var text: String = "",

    @SerializedName("poster")
    var poster: String = "",

    @SerializedName("date")
    var date: String = "",

    @SerializedName("source")
    var source: String = "",

    @SerializedName("link")
    var link: String = ""
)