package com.infinity_coder.divcalendar.domain.models

import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel

data class Payment(
    val name: String,
    val logo: String,
    val count: Int,
    var dividends: Double,
    val date: String,
    val currency: String,
    var colorLogo: Int
) {
    companion object {
        fun from(paymentNetworkModel: PaymentNetworkModel, securityPackage: SecurityPackageDbModel) =
            Payment(
                name = securityPackage.name,
                logo = securityPackage.logo, // TODO сделать с сервера
                count = securityPackage.count,
                dividends = paymentNetworkModel.dividends * securityPackage.count,
                date = paymentNetworkModel.date,
                currency = paymentNetworkModel.currency,
                colorLogo = -1
            )
    }
}