package com.infinity_coder.divcalendar.data.network

import com.infinity_coder.divcalendar.data.network.model.SecurityNetworkModel
import retrofit2.http.GET
import retrofit2.http.Query

interface DivCalendarApi {
    @GET("search/")
    suspend fun searchSecurities(
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("market") market: String,
        @Query("limit") limit: Int? = null
    ): List<SecurityNetworkModel>
}