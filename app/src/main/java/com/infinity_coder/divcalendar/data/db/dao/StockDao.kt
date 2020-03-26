package com.infinity_coder.divcalendar.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.infinity_coder.divcalendar.data.db.model.StockPackageDbModel

@Dao
abstract class StockDao {

    @Query("SELECT * FROM ${StockPackageDbModel.TABLE_NAME} WHERE ${StockPackageDbModel.COLUMN_SEC_ID}=:secid LIMIT 1")
    abstract suspend fun getStockPackage(secid: String): StockPackageDbModel?

    @Query("SELECT * FROM ${StockPackageDbModel.TABLE_NAME}")
    abstract fun getAllStockPackageLiveData(): LiveData<List<StockPackageDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addStockPackage(stockPackage: StockPackageDbModel)

    @Delete
    abstract suspend fun deleteStockPackage(stockPackage: StockPackageDbModel)

    @Query("DELETE FROM ${StockPackageDbModel.TABLE_NAME} WHERE ${StockPackageDbModel.COLUMN_SEC_ID}=:secid")
    abstract suspend fun deleteStockPackage(secid: String)
}