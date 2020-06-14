package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import com.infinity_coder.divcalendar.data.repositories.SearchRepository
import com.infinity_coder.divcalendar.domain.models.QueryGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SearchInteractor {

    suspend fun search(queryGroup: QueryGroup, limit: Int = DEFAULT_LIMIT): Flow<List<SecurityNetModel>> {
        return if (queryGroup.query.length >= MIN_QUERY_LENGTH) {
            SearchRepository.search(queryGroup.query, queryGroup.securityType, queryGroup.market, limit).map { mapSecurities(it) }
        } else {
            flowOf(emptyList())
        }
    }

    private fun mapSecurities(securities: List<SecurityNetModel>): List<SecurityNetModel> {
        securities.forEach {
            if (it.isin.isBlank())
                it.isin = it.ticker
        }
        return securities
    }

    companion object {
        private const val DEFAULT_LIMIT = 20

        private const val MIN_QUERY_LENGTH = 1
    }
}