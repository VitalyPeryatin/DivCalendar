package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object SearchRepository {

    private val divCalendarApi = RetrofitService.divCalendarApi

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun search(
        query: String,
        type: String,
        market: String,
        limit: Int
    ): Flow<List<SecurityNetModel>> = flow {
        emit(divCalendarApi.searchSecurities(query, type, market, limit))
    }
}