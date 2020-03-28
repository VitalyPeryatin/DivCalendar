package com.infinity_coder.divcalendar.presentation.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel

data class PaymentPresentationModel(
    val name:String,
    val logo:String,
    val count:String,
    val dividends:String,
    val date:String
): IComparableItem {

    companion object {
        fun from(groupPayment: Pair<String, List<PaymentNetworkModel>>) =
            groupPayment.second.map {
                PaymentPresentationModel(
                    name = it.name,
                    logo = it.logo,
                    count = it.count.toString(),
                    dividends = "${it.dividends}₽",
                    date = it.date
                )
            }
    }

    override fun id() = name

    override fun content() = this

}