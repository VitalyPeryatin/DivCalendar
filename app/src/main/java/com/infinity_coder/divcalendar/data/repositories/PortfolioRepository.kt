package com.infinity_coder.divcalendar.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.presentation.App
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
object PortfolioRepository {

    private val portfolioDao = DivCalendarDatabase.roomDatabase.portfolioDao

    private const val PORTFOLIO_PREF_NAME = "Portfolio"
    private const val PREF_CURRENT_PORTFOLIO = "current_portfolio"
    private val portfolioPreferences = App.instance.getSharedPreferences(
        PORTFOLIO_PREF_NAME,
        Context.MODE_PRIVATE
    )

    suspend fun addPortfolio(portfolioName: String) {
        val portfolio = PortfolioDbModel(portfolioName)
        portfolioDao.insertPortfolio(portfolio)
    }

    fun getAllPortfolios(): Flow<List<PortfolioDbModel>> {
        return portfolioDao.getAllPortfoliosFlow().filterNotNull().distinctUntilChanged()
    }

    suspend fun getAllPortfolioNames(): List<String> {
        return portfolioDao.getPortfolioNames()
    }

    private suspend fun getPortfolioByName(portfolioName: String): PortfolioDbModel? {
        return getPortfolioWithSecurities(portfolioName).first()
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
        val portfolio = getPortfolioByName(oldName)
        if (portfolio != null) {
            portfolio.name = newName
            if (getCurrentPortfolioName() == oldName) {
                setCurrentPortfolio(newName)
            }
            portfolioDao.updatePortfolio(portfolio)
        }
    }

    fun getAllPortfoliosWithSecurities(): Flow<List<PortfolioDbModel>> = flow {
        emit(portfolioDao.getAllPortfoliosWithSecurities())
    }

    suspend fun getPortfolioCount(): Int {
        return portfolioDao.getPortfolioCount()
    }

    private fun getPortfolioWithSecurities(name: String): Flow<PortfolioDbModel> {
        return getAllPortfoliosWithSecurities().map { it.first { portfolio -> portfolio.name == name } }
            .distinctUntilChanged()
    }
}