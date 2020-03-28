package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel

object PaymentRepository {

    suspend fun loadAllPayments(): List<PaymentNetworkModel> {
        val mockData = mutableListOf<PaymentNetworkModel>()
        for (i in 0 until 12) {
            for (j in 1 until 5) {
                mockData.add(
                    PaymentNetworkModel(
                        "Яндекс",
                        "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/20/74/3c2996c034c70685ec736cd563e55dd6.png",
                        15,
                        174.05,
                        "2020-${if (i.toString().length == 1) "0$i" else i.toString()}-${if (j.toString().length == 1) "0$j" else j.toString()}"
                    )
                )
            }
        }
        return mockData
    }

}