package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.domain.models.DataSourceModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object DataSourcesRepository {
    suspend fun getDataSources(): Flow<List<DataSourceModel>> {
        return flow {
            val dataSources = listOf(
                DataSourceModel(
                    iconResId = R.drawable.ic_logo_dohod,
                    name = "Доход",
                    link = "https://www.dohod.ru/"
                ), DataSourceModel(
                    iconResId = R.drawable.ic_logo_smartlab,
                    name = "SmartLab",
                    link = "https://smart-lab.ru/"
                ), DataSourceModel(
                    iconResId = R.drawable.ic_logo_tinkoff,
                    name = "Tinkoff Инвестиции",
                    link = "https://www.tinkoff.ru/invest/"
                ), DataSourceModel(
                    iconResId = R.drawable.ic_logo_investfunds,
                    name = "InvestFunds",
                    link = "https://investfunds.ru/"
                )
            )
            emit(dataSources)
        }
    }
}