package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.repositories.datasources.DataSourcesRepository
import com.infinity_coder.divcalendar.data.repositories.datasources.models.DataSourceModel
import kotlinx.coroutines.flow.Flow

class DataSourcesInteractor {
    suspend fun getDataSources(): Flow<List<DataSourceModel>> {
        return DataSourcesRepository.getDataSources()
    }
}