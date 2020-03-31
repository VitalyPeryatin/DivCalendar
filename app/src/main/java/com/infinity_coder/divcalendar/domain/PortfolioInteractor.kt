package com.infinity_coder.divcalendar.domain

import androidx.lifecycle.LiveData
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortSecurityNetworkModel
import com.infinity_coder.divcalendar.data.repositories.PortfolioRepository

class PortfolioInteractor {
    suspend fun loadAllSecurities(): List<ShortSecurityNetworkModel> {
        return PortfolioRepository.loadAllSecurities()
    }

    fun loadAllSecurityPackages(): LiveData<List<SecurityPackageDbModel>> {
        return PortfolioRepository.loadAllSecurityPackages()
    }

    suspend fun changeSecurityPackage(securityPackage: SecurityPackageDbModel) {
        if (securityPackage.count <= 0) {
            PortfolioRepository.deleteSecurityPackage(securityPackage)
        } else {
            PortfolioRepository.addSecurityPackage(securityPackage)
        }
    }

    suspend fun appendSecurityPackage(newSecurityPackage: SecurityPackageDbModel) {
        var securitiesPackage = PortfolioRepository.getSecurityPackage(newSecurityPackage.secid)
        if (securitiesPackage == null) {
            securitiesPackage = newSecurityPackage
        } else {
            securitiesPackage.count += newSecurityPackage.count
            securitiesPackage.totalPrice += newSecurityPackage.totalPrice
        }
        PortfolioRepository.addSecurityPackage(securitiesPackage)
    }
}