package com.infinity_coder.divcalendar.data.repositories

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.palette.graphics.Palette
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import java.io.InputStream
import java.net.URL

object SecurityRepository {

    private val securityDao = DivCalendarDatabase.roomDatabase.securityDao

    suspend fun deleteSecurityPackage(securityPackage: SecurityDbModel) {
        securityDao.deleteSecurityPackage(securityPackage)
    }

    suspend fun addSecurityPackage(securityPackage: SecurityDbModel) {
        securityDao.insertSecurityPackage(securityPackage)
    }

    suspend fun updateSecurityPackage(securityPackage: SecurityDbModel) {
        securityDao.updateSecurityPackage(securityPackage)
    }

    suspend fun getSecurityPackage(portfolioId: Long, secid: String): SecurityDbModel? {
        return securityDao.getSecurityPackage(portfolioId, secid)
    }

    suspend fun getSecurityCount(portfolioId: Long): Int {
        return securityDao.getSecurityCountForPortfolio(portfolioId)
    }

    fun getColorForSecurityLogo(logo: String): Int {
        return try {
            val inputStream: InputStream = URL(logo).openStream()
            val image = BitmapFactory.decodeStream(inputStream)
            Palette.from(image).generate().getDominantColor(0)
        } catch (e: Exception) {
            Color.BLACK
        }
    }
}