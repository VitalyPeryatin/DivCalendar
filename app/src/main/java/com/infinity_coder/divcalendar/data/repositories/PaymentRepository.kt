package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object PaymentRepository {

    suspend fun loadAllPayments(): Flow<List<PaymentNetworkModel>> {
        return flowOf(listOf(
            PaymentNetworkModel(
                "Яндекс",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/20/74/3c2996c034c70685ec736cd563e55dd6.png",
                15,
                174.05,
                "2020-01-04",
                "USD"
            ),
            PaymentNetworkModel(
                "Яндекс",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/20/74/3c2996c034c70685ec736cd563e55dd6.png",
                15,
                174.05,
                "2020-03-04",
                "RUB"
            ),
            PaymentNetworkModel(
                "Яндекс",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/20/74/3c2996c034c70685ec736cd563e55dd6.png",
                15,
                174.05,
                "2020-12-04",
                "RUB"
            ),
            PaymentNetworkModel(
                "Яндекс",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/20/74/3c2996c034c70685ec736cd563e55dd6.png",
                15,
                174.05,
                "2020-08-04",
                "RUB"
            ),
            PaymentNetworkModel(
                "Berkshire Hathaway",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/17/27/ea801305f1b36bbaa63f4ed481522fed.png",
                30,
                342.4,
                "2020-01-21",
                "RUB"
            ),
            PaymentNetworkModel(
                "Netflix",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/14/92/ef18f9c42fac467a8502ec85b1a0159b.png",
                5,
                1234.1,
                "2020-05-11",
                "RUB"
            ),
            PaymentNetworkModel(
                "MTC",
                "https://s3-symbol-logo.tradingview.com/mts--big.svg",
                5,
                123.1,
                "2020-04-11",
                "RUB"
            ),
            PaymentNetworkModel(
                "Welltower",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/50/95/f1e15de0b09ae756330dcb3577957279.png",
                14,
                202.3,
                "2020-05-21",
                "RUB"
            ),
            PaymentNetworkModel(
                "Starbucks",
                "https://s0.rbk.ru/emitent_pics/resized/80x80_crop/images/57/14/aea38279dbc2f56d42174fddf90686d9.png",
                30,
                241.3,
                "2020-08-21",
                "RUB"
            )
        ))
    }
}