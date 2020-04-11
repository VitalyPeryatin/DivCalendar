package com.infinity_coder.divcalendar.data.network.model

import com.google.gson.annotations.SerializedName

data class BodyPostNetworkModel(
    @SerializedName("securities")
    val securities: List<String>,

    @SerializedName("limit")
    val limit: Int,

    @SerializedName("offset")
    val offset: Int
)