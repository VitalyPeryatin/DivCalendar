package com.infinity_coder.divcalendar.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.PortfolioWithSecurities
import com.infinity_coder.divcalendar.presentation.App
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalCoroutinesApi::class)
object PortfolioRepository {

    private val portfolioDao = DivCalendarDatabase.roomDatabase.portfolioDao

    private const val PORTFOLIO_PREF_NAME = "Portfolio"
    private const val CURRENT_PORTFOLIO_NAME_KEY = "current_portfolio"
    private val portfolioPreferences = App.instance.getSharedPreferences(PORTFOLIO_PREF_NAME, Context.MODE_PRIVATE)

    suspend fun addPortfolio(portfolioName: String) {
        val portfolio = PortfolioDbModel(portfolioName)
        portfolioDao.insertPortfolio(portfolio)
    }

    fun getAllPortfolios(): Flow<List<PortfolioDbModel>> {
        return portfolioDao.getAllPortfolios()
            .filterNotNull()
            .distinctUntilChanged()
    }

    private suspend fun getPortfolioByName(portfolioName: String): PortfolioDbModel? {
        return getPortfolioWithSecurities(portfolioName).first().portfolio
    }

    fun setCurrentPortfolio(name: String) {
        portfolioPreferences.edit {
            putString(CURRENT_PORTFOLIO_NAME_KEY, name)
        }
    }

    fun getCurrentPortfolio(): String {
        return portfolioPreferences.getString(CURRENT_PORTFOLIO_NAME_KEY, App.DEFAULT_PORTFOLIO_NAME)!!
    }

    suspend fun deletePortfolio(name: String) {
        portfolioDao.deletePortfolio(name)
    }

    suspend fun renamePortfolio(oldName: String, newName: String) {
        val portfolio = getPortfolioByName(oldName)
        if (portfolio != null) {
            portfolio.name = newName
            portfolioDao.updatePortfolio(portfolio)
        }
    }

    fun getPortfolioWithSecurities(name: String): Flow<PortfolioWithSecurities> {
        return portfolioDao.getPortfolioWithSecurities(name)
            .filterNotNull()
            .distinctUntilChanged()
    }
}