package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.SecurityNetworkModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object SearchRepository {

    private val divCalendarApi = RetrofitService.divCalendarApi

    suspend fun search(query: String, type: String, market: String, limit: Int): Flow<List<SecurityNetworkModel>> = flowOf(
        divCalendarApi.searchSecurities(query, type, market, limit)
    )
}