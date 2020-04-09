package com.infinity_coder.divcalendar.data.db.dao

import androidx.room.*
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel

@Dao
abstract class SecurityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertSecurityPackage(securityPackage: SecurityPackageDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun updateSecurityPackage(securityPackage: SecurityPackageDbModel)

    @Query("SELECT * FROM ${SecurityPackageDbModel.TABLE_NAME} WHERE ${SecurityPackageDbModel.COLUMN_PORTFOLIO_NAME} = :portfolioName AND ${SecurityPackageDbModel.COLUMN_SEC_ID} = :secId")
    abstract suspend fun getSecurityPackage(portfolioName: String, secId: String): SecurityPackageDbModel?

    @Delete
    abstract suspend fun deleteSecurityPackage(securityPackage: SecurityPackageDbModel)
}