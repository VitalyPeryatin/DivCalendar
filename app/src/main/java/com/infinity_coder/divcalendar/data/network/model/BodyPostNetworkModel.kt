package com.infinity_coder.divcalendar.data.network.model

data class BodyPostNetworkModel(
    val securities: List<String>,
    val limit: Int,
    val offset: Int
)