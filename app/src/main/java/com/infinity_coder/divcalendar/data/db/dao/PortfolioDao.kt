package com.infinity_coder.divcalendar.data.db.dao

import androidx.room.*
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.PortfolioWithSecurities
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PortfolioDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertPortfolio(portfolio: PortfolioDbModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun updatePortfolio(portfolio: PortfolioDbModel)

    @Query("DELETE FROM ${PortfolioDbModel.TABLE_NAME} WHERE ${PortfolioDbModel.COLUMN_NAME} = :name")
    abstract suspend fun deletePortfolio(name: String)

    @Query("SELECT * FROM ${PortfolioDbModel.TABLE_NAME}")
    abstract fun getAllPortfolios(): Flow<List<PortfolioDbModel>?>

    @Transaction
    @Query("SELECT * FROM ${PortfolioDbModel.TABLE_NAME} WHERE ${PortfolioDbModel.COLUMN_NAME} = :name LIMIT 1")
    abstract fun getPortfolioWithSecurities(name: String): Flow<PortfolioWithSecurities?>

    @Transaction
    @Query("SELECT * FROM ${PortfolioDbModel.TABLE_NAME}")
    abstract fun getAllPortfoliosWithSecurities(): Flow<List<PortfolioWithSecurities>>

    @Query("SELECT ${PortfolioDbModel.COLUMN_NAME} FROM ${PortfolioDbModel.TABLE_NAME}")
    abstract suspend fun getPortfolioNames(): List<String>

    @Query("SELECT count(*) FROM ${PortfolioDbModel.TABLE_NAME}")
    abstract suspend fun getPortfolioCount(): Int

    @Transaction
    open suspend fun insertPortfolioWithSecurities(portfolio: PortfolioWithSecurities) {
        insertPortfolio(portfolio.portfolio)
        for (security in portfolio.securities) {
            addSecurityPackage(security)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addSecurityPackage(securityPackage: SecurityPackageDbModel)
}