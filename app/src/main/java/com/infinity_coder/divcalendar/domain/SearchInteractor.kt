package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.network.model.SecurityNetworkModel
import com.infinity_coder.divcalendar.data.repositories.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchInteractor {

    suspend fun search(
        query: String,
        type: String,
        market: String,
        limit: Int = DEFAULT_LIMIT
    ): Flow<List<SecurityNetworkModel>> =
        SearchRepository.search(query, type, market, limit)

    companion object {
        private const val DEFAULT_LIMIT = 20
    }
}