package com.infinity_coder.divcalendar.data.network

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object JsoupService {

    private const val DOHOD_URL = "https://www.dohod.ru/"

    // Анализ выплаты дивидендов
    fun getStock(ticker: String): String {
        val doc: Document = Jsoup.connect(DOHOD_URL + "ik/analytics/dividend/" + ticker).get()
        val rows = doc.select("table.content-table")[1].select("tr")
        return rows.toString()
    }
}