package com.infinity_coder.divcalendar.data.repositories

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.palette.graphics.Palette
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import java.io.InputStream
import java.net.URL
import kotlin.random.Random

object SecurityRepository {

    private const val COLOR_RANDOM_START = 50
    private const val COLOR_RANDOM_END = 200

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

    suspend fun getSecurity(portfolioId: Long, isin: String): SecurityDbModel? {
        return securityDao.getSecurity(portfolioId, isin)
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
            generateColor()
        }
    }

    private fun generateColor(): Int {
        val r = Random.nextInt(COLOR_RANDOM_START, COLOR_RANDOM_END)
        val g = Random.nextInt(COLOR_RANDOM_START, COLOR_RANDOM_END)
        val b = Random.nextInt(COLOR_RANDOM_START, COLOR_RANDOM_END)
        return Color.rgb(r, g, b)
    }
}