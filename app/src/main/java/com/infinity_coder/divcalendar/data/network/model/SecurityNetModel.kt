package com.infinity_coder.divcalendar.data.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SecurityNetModel(
    @SerializedName("isin")
    var isin: String = "",

    @SerializedName("ticker")
    val ticker: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("logo")
    val logo: String = "",

    @SerializedName("yield")
    val yearYield: Float = 0f,

    @SerializedName("exchange")
    val exchange: String = "",

    @SerializedName("currency")
    val currency: String = "",

    @SerializedName("price")
    val currentPrice: Double? = null,

    var market: String = SECURITY_MARKET_RUSSIAN
) : Parcelable {

    companion object {
        const val SECURITY_TYPE_STOCK = "stock"
        const val SECURITY_TYPE_BOND = "bond"

        const val SECURITY_MARKET_RUSSIAN = "russian"
        const val SECURITY_MARKET_FOREIGN = "foreign"

        const val SECURITY_EXCHANGE = "MOEX"

        val SECURITY_MARKETS = arrayOf(SECURITY_MARKET_RUSSIAN, SECURITY_MARKET_FOREIGN)
        val SECURITY_TYPES = arrayOf(SECURITY_TYPE_STOCK, SECURITY_TYPE_BOND)
    }
}