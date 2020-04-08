package com.infinity_coder.divcalendar.domain.models

import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel

data class Payment(
    val name:String,
    val logo:String,
    val count:Int,
    val dividends:Double,
    val date:String,
    val currency: String,
    var colorLogo:Int
){
    companion object{
        fun from(paymentNetworkModel:PaymentNetworkModel) =
            Payment(
                name = paymentNetworkModel.name,
                logo = paymentNetworkModel.logo,
                count = paymentNetworkModel.count,
                dividends = paymentNetworkModel.dividends,
                date = paymentNetworkModel.date,
                currency = paymentNetworkModel.currency,
                colorLogo = -1
            )
    }
}