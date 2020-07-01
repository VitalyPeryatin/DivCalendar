package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.repositories.DataSourcesRepository
import com.infinity_coder.divcalendar.domain.models.DataSourceModel
import kotlinx.coroutines.flow.Flow

class DataSourcesInteractor {
    suspend fun getDataSources(): Flow<List<DataSourceModel>> {
        return DataSourcesRepository.getDataSources()
    }
}