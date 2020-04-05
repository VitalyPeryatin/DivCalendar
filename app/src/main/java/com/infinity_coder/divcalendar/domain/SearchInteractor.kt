package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.network.model.SecurityNetworkModel
import com.infinity_coder.divcalendar.data.repositories.SearchRepository
import com.infinity_coder.divcalendar.presentation.search.model.QueryGroup
import kotlinx.coroutines.flow.Flow

class SearchInteractor {

    suspend fun search(
        queryGroup: QueryGroup,
        limit: Int = DEFAULT_LIMIT
    ): Flow<List<SecurityNetworkModel>> =
        SearchRepository.search(queryGroup.query, queryGroup.securityType, queryGroup.market, limit)

    companion object {
        private const val DEFAULT_LIMIT = 20
    }
}