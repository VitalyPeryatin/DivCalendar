package com.infinity_coder.divcalendar.data.db.dao

import androidx.room.*
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PortfolioDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertPortfolio(portfolio: PortfolioDbModel)

    @Query("UPDATE ${PortfolioDbModel.TABLE_NAME} SET ${PortfolioDbModel.COLUMN_NAME} = :newName WHERE ${PortfolioDbModel.COLUMN_NAME} = :oldName")
    abstract suspend fun renamePortfolio(oldName: String, newName: String)

    @Query("DELETE FROM ${PortfolioDbModel.TABLE_NAME} WHERE ${PortfolioDbModel.COLUMN_NAME} = :name")
    abstract suspend fun deletePortfolio(name: String)

    @Query("SELECT * FROM ${PortfolioDbModel.TABLE_NAME} WHERE ${PortfolioDbModel.COLUMN_NAME} = :name LIMIT 1")
    abstract suspend fun getPortfolioByName(name: String): PortfolioDbModel?

    @Transaction
    open suspend fun getPortfolioWithSecurities(name: String): PortfolioDbModel? {
        return getPortfolioWithSecuritiesByName(name)
    }

    @Transaction
    open suspend fun getAllPortfoliosWithSecurities(): List<PortfolioDbModel> {
        return getPortfolioNames().mapNotNull { name ->
            getPortfolioWithSecuritiesByName(name)
        }
    }

    private suspend fun getPortfolioWithSecuritiesByName(name: String): PortfolioDbModel? {
        val portfolio = getPortfolioByName(name) ?: return null
        portfolio.securities = getSecurities(portfolio.id)
        return portfolio
    }

    @Query("SELECT ${PortfolioDbModel.COLUMN_ID} FROM ${PortfolioDbModel.TABLE_NAME} WHERE ${PortfolioDbModel.COLUMN_NAME} = :name LIMIT 1")
    abstract suspend fun getPortfolioId(name: String): Long

    @Query("SELECT * FROM ${PortfolioDbModel.TABLE_NAME}")
    abstract fun getAllPortfoliosFlow(): Flow<List<PortfolioDbModel>>

    @Query("SELECT * FROM ${SecurityDbModel.TABLE_NAME} WHERE ${SecurityDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId")
    abstract suspend fun getSecurities(portfolioId: Long): List<SecurityDbModel>

    @Query("SELECT ${PortfolioDbModel.COLUMN_NAME} FROM ${PortfolioDbModel.TABLE_NAME}")
    abstract suspend fun getPortfolioNames(): List<String>

    @Query("SELECT count(*) FROM ${PortfolioDbModel.TABLE_NAME}")
    abstract suspend fun getPortfolioCount(): Int

    @Query("SELECT * FROM ${PortfolioDbModel.TABLE_NAME}")
    abstract suspend fun getAllPortfolios(): List<PortfolioDbModel>
}