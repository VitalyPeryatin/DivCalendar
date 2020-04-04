package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel

object PaymentRepository {

    suspend fun loadAllPayments(): List<PaymentNetworkModel> {
        return listOf(
            PaymentNetworkModel(
                "Яндекс",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/20/74/3c2996c034c70685ec736cd563e55dd6.png",
                15,
                174.05,
                "2020-01-04"
            ),
            PaymentNetworkModel(
                "Яндекс",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/20/74/3c2996c034c70685ec736cd563e55dd6.png",
                15,
                174.05,
                "2020-03-04"
            ),
            PaymentNetworkModel(
                "Яндекс",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/20/74/3c2996c034c70685ec736cd563e55dd6.png",
                15,
                174.05,
                "2020-08-04"
            ),
            PaymentNetworkModel(
                "Berkshire Hathaway",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/17/27/ea801305f1b36bbaa63f4ed481522fed.png",
                30,
                342.4,
                "2020-01-21"
            ),
            PaymentNetworkModel(
                "Netflix",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/14/92/ef18f9c42fac467a8502ec85b1a0159b.png",
                5,
                1234.1,
                "2020-05-11"
            ),
            PaymentNetworkModel(
                "MTC",
                "https://s3-symbol-logo.tradingview.com/mts--big.svg",
                5,
                123.1,
                "2020-04-11"
            ),
            PaymentNetworkModel(
                "Welltower",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/50/95/f1e15de0b09ae756330dcb3577957279.png",
                14,
                202.3,
                "2020-05-21"
            ),
            PaymentNetworkModel(
                "Starbucks",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/57/14/aea38279dbc2f56d42174fddf90686d9.png",
                30,
                241.3,
                "2020-08-21"
            )
        )
    }
}