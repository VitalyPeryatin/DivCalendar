package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.repositories.SecurityRepository

class SecurityInteractor {

    suspend fun changeSecurityPackage(securityPackage: SecurityPackageDbModel) {
        if (securityPackage.count <= 0) {
            SecurityRepository.deleteSecurityPackage(securityPackage)
        } else {
            SecurityRepository.updateSecurityPackage(securityPackage)
        }
    }

    suspend fun appendSecurityPackage(portfolioName: String, newSecurityPackage: SecurityPackageDbModel) {
        val securitiesPackage = SecurityRepository.getSecurityPackage(newSecurityPackage.secid)
        if (securitiesPackage == null) {
            newSecurityPackage.portfolioName = portfolioName
            SecurityRepository.addSecurityPackage(newSecurityPackage)
        } else {
            securitiesPackage.count += newSecurityPackage.count
            securitiesPackage.totalPrice += newSecurityPackage.totalPrice
            SecurityRepository.updateSecurityPackage(securitiesPackage)
        }
    }
}