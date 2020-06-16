package com.infinity_coder.divcalendar.data.db.dao

import androidx.room.*
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel

@Dao
abstract class SecurityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertSecurityPackage(securityPackage: SecurityDbModel)

    @Update
    abstract suspend fun updateSecurityPackage(securityPackage: SecurityDbModel)

    @Query("SELECT * FROM ${SecurityDbModel.TABLE_NAME} WHERE ${SecurityDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId AND ${SecurityDbModel.COLUMN_ISIN} = :isin")
    abstract suspend fun getSecurity(portfolioId: Long, isin: String): SecurityDbModel?

    @Query("SELECT * FROM ${SecurityDbModel.TABLE_NAME} WHERE ${SecurityDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId")
    abstract suspend fun getSecurityPackagesForPortfolio(portfolioId: Long): List<SecurityDbModel>

    @Query("SELECT count(*) FROM ${SecurityDbModel.TABLE_NAME} WHERE ${SecurityDbModel.COLUMN_PORTFOLIO_ID} = :portfolioId")
    abstract suspend fun getSecurityCountForPortfolio(portfolioId: Long): Int

    @Delete
    abstract suspend fun deleteSecurityPackage(securityPackage: SecurityDbModel)
}