package com.infinity_coder.divcalendar.data.network

import com.infinity_coder.divcalendar.data.network.model.*
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
    ): List<SecurityNetworkModel>

    @POST("posts")
    suspend fun fetchPosts(@Body body: PostNetworkModel.Request): List<PostNetworkModel.Response>

    @GET("rate")
    suspend fun fetchCurrencyRate(): CurrencyRateNetworkModel

    @POST("payments")
    suspend fun fetchPayments(@Body body: PaymentNetworkModel.Request): List<PaymentNetworkModel.Response>
}