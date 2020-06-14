package com.infinity_coder.divcalendar.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.domain.models.SortType
import com.infinity_coder.divcalendar.presentation.App
import com.infinity_coder.divcalendar.presentation._common.extensions.getNotNullString
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
object PortfolioRepository {

    private val portfolioDao = DivCalendarDatabase.roomDatabase.portfolioDao

    private const val PORTFOLIO_PREF_NAME = "Portfolio"
    private const val PREF_CURRENT_PORTFOLIO = "current_portfolio"
    private const val PREF_CURRENT_TYPE_SORT = "current_type_sort"
    private val portfolioPreferences = App.instance.getSharedPreferences(PORTFOLIO_PREF_NAME, Context.MODE_PRIVATE)

    suspend fun addPortfolio(portfolioName: String) {
        val portfolio = PortfolioDbModel(portfolioName)
        portfolioDao.insertPortfolio(portfolio)
    }

    fun getAllPortfolios(): Flow<List<PortfolioDbModel>> {
        return portfolioDao.getAllPortfoliosFlow()
            .filterNotNull()
            .distinctUntilChanged()
    }

    suspend fun getAllPortfoliosWithSecurity(): List<PortfolioDbModel> {
        return portfolioDao.getAllPortfoliosWithSecurities()
    }

    suspend fun getAllPortfolioNames(): List<String> {
        return portfolioDao.getPortfolioNames()
    }

    fun setCurrentPortfolio(name: String) {
        portfolioPreferences.edit {
            putString(PREF_CURRENT_PORTFOLIO, name)
        }
    }

    fun getCurrentPortfolioName(): String {
        return portfolioPreferences.getString(PREF_CURRENT_PORTFOLIO, "")!!
    }

    suspend fun getCurrentPortfolioId(): Long {
        val portfolioName = getCurrentPortfolioName()
        return portfolioDao.getPortfolioId(portfolioName)
    }

    suspend fun deletePortfolio(name: String) {
        portfolioDao.deletePortfolio(name)
    }

    suspend fun renamePortfolio(oldName: String, newName: String) {
        portfolioDao.renamePortfolio(oldName, newName)
    }

    fun getPortfolioWithSecurities(name: String): Flow<PortfolioDbModel?> = flow {
        emit(portfolioDao.getPortfolioWithSecurities(name))
    }

    suspend fun getPortfolioCount(): Int {
        return portfolioDao.getPortfolioCount()
    }

    fun setCurrentSortType(sortType: SortType) {
        portfolioPreferences.edit {
            putString(PREF_CURRENT_TYPE_SORT, sortType.name)
        }
    }

    fun getCurrentSortType(): SortType {
        val name = portfolioPreferences.getNotNullString(PREF_CURRENT_TYPE_SORT, SortType.PAYMENT_DATE.name)
        return SortType.getSortTypeByName(name)
    }
}