package com.infinity_coder.divcalendar.data.db.dao

import androidx.room.*
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel

@Dao
abstract class PortfolioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertPortfolio(portfolio: PortfolioDbModel)

    @Query("SELECT * FROM ${PortfolioDbModel.TABLE_NAME} WHERE ${PortfolioDbModel.COLUMN_NAME} = :name")
    abstract suspend fun getPortfolioByName(name: String): PortfolioDbModel?

    @Query("SELECT * FROM ${PortfolioDbModel.TABLE_NAME}")
    abstract suspend fun getAllPortfolios(): List<PortfolioDbModel>

    @Delete
    abstract suspend fun deletePortfolio(portfolio: PortfolioDbModel)
}