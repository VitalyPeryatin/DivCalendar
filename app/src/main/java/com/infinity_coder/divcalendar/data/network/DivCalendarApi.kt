package com.infinity_coder.divcalendar.data.network

import com.infinity_coder.divcalendar.data.network.model.CurrencyRateNetModel
import com.infinity_coder.divcalendar.data.network.model.NewsPostNetModel
import com.infinity_coder.divcalendar.data.network.model.PaymentNetModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DivCalendarApi {
    @GET("search")
    suspend fun searchSecurities(
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("market") market: String,
        @Query("limit") limit: Int? = null
    ): List<SecurityNetModel>

    @POST("posts")
    suspend fun fetchPosts(@Body body: NewsPostNetModel.Request): List<NewsPostNetModel.Response>

    @GET("rate")
    suspend fun fetchCurrencyRate(): CurrencyRateNetModel

    @POST("payments")
    suspend fun fetchPayments(@Body body: PaymentNetModel.Request): List<PaymentNetModel.Response>
}