package com.infinity_coder.divcalendar.domain

import androidx.lifecycle.LiveData
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.repositories.SecurityRepository

class SecurityInteractor {

    fun loadAllSecurityPackages(): LiveData<List<SecurityPackageDbModel>> {
        return SecurityRepository.loadAllSecurityPackages()
    }

    suspend fun changeSecurityPackage(securityPackage: SecurityPackageDbModel) {
        if (securityPackage.count <= 0) {
            SecurityRepository.deleteSecurityPackage(securityPackage)
        } else {
            SecurityRepository.addSecurityPackage(securityPackage)
        }
    }

    suspend fun appendSecurityPackage(newSecurityPackage: SecurityPackageDbModel) {
        var securitiesPackage = SecurityRepository.getSecurityPackage(newSecurityPackage.secid)
        if (securitiesPackage == null) {
            securitiesPackage = newSecurityPackage
        } else {
            securitiesPackage.count += newSecurityPackage.count
            securitiesPackage.totalPrice += newSecurityPackage.totalPrice
        }
        SecurityRepository.addSecurityPackage(securitiesPackage)
    }
}