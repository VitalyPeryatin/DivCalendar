package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object SearchRepository {

    private val divCalendarApi
        get() = RetrofitService.divCalendarApi

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun searchFlow(
        query: String,
        type: String,
        market: String,
        limit: Int
    ): Flow<List<SecurityNetModel>> = flow {
        val securities = divCalendarApi.searchSecurities(query, type, market, limit)
        securities.forEach { it.market = market }
        emit(securities)
    }

    suspend fun search(
        query: String,
        type: String,
        market: String,
        limit: Int
    ):List<SecurityNetModel>{
        val securities = divCalendarApi.searchSecurities(query, type, market, limit)
        securities.forEach { it.market = market }
        return securities
    }
}