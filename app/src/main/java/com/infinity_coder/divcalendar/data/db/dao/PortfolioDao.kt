package com.infinity_coder.divcalendar.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel

@Dao
abstract class PortfolioDao {

    @Query("SELECT * FROM ${SecurityPackageDbModel.TABLE_NAME} WHERE ${SecurityPackageDbModel.COLUMN_SEC_ID}=:secid LIMIT 1")
    abstract suspend fun getSecurityPackage(secid: String): SecurityPackageDbModel?

    @Query("SELECT * FROM ${SecurityPackageDbModel.TABLE_NAME}")
    abstract fun getAllSecuritiesPackageLiveData(): LiveData<List<SecurityPackageDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addSecurityPackage(securityPackage: SecurityPackageDbModel)

    @Delete
    abstract suspend fun deleteSecurityPackage(securityPackage: SecurityPackageDbModel)

    @Query("DELETE FROM ${SecurityPackageDbModel.TABLE_NAME} WHERE ${SecurityPackageDbModel.COLUMN_SEC_ID}=:secid")
    abstract suspend fun deleteSecurityPackage(secid: String)
}