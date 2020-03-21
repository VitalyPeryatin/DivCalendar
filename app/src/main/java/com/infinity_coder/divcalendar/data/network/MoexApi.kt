package com.infinity_coder.divcalendar.data.network

import com.infinity_coder.divcalendar.data.network.model.ShortStockList
import retrofit2.http.GET

interface MoexApi {

    @GET("iss/statistics/engines/stock/quotedsecurities.json")
    suspend fun getStocks(): ShortStockList

}