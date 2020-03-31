package com.infinity_coder.divcalendar.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.infinity_coder.divcalendar.data.db.model.SecPackageDbModel

@Dao
abstract class StockDao {

    @Query("SELECT * FROM ${SecPackageDbModel.TABLE_NAME} WHERE ${SecPackageDbModel.COLUMN_SEC_ID}=:secid LIMIT 1")
    abstract suspend fun getStockPackage(secid: String): SecPackageDbModel?

    @Query("SELECT * FROM ${SecPackageDbModel.TABLE_NAME}")
    abstract fun getAllStockPackageLiveData(): LiveData<List<SecPackageDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addStockPackage(stockPackage: SecPackageDbModel)

    @Delete
    abstract suspend fun deleteStockPackage(stockPackage: SecPackageDbModel)

    @Query("DELETE FROM ${SecPackageDbModel.TABLE_NAME} WHERE ${SecPackageDbModel.COLUMN_SEC_ID}=:secid")
    abstract suspend fun deleteStockPackage(secid: String)
}