package com.infinity_coder.divcalendar.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.edit
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.BuildConfig
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.presentation.App
import com.infinity_coder.divcalendar.presentation._common.logFile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

object SettingsRepository {

    private const val TAXES_PREFERENCES_NAME = "Settings"
    private const val SUBSCRIPTION_PREFERENCES_NAME = "SubscriptionFile"
    private const val PREF_INCLUDE_TAXES = "is_include_taxes"
    private const val PREF_HAS_SUBSCRIPTION = "has_subscription"
    const val TELEGRAM_GROUP_LINK = "https://t.me/joinchat/H2bVmxsrivVBQ0rqv0AWIg"

    @SuppressLint("ConstantLocale")
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SS", Locale.getDefault())

    private val taxesPreferences = App.instance.getSharedPreferences(TAXES_PREFERENCES_NAME, Context.MODE_PRIVATE)

    private val database = FirebaseDatabase.getInstance()
    private val storageReference = FirebaseStorage.getInstance().reference

    private val portfolioDao = DivCalendarDatabase.roomDatabase.portfolioDao
    private val newsDao = DivCalendarDatabase.roomDatabase.newsDao
    private val securityDao = DivCalendarDatabase.roomDatabase.securityDao
    private val paymentDao = DivCalendarDatabase.roomDatabase.paymentDao

    fun saveIsIncludeTaxes(isAccountTaxes: Boolean) {
        taxesPreferences.edit {
            putBoolean(PREF_INCLUDE_TAXES, isAccountTaxes)
        }
    }

    fun isIncludeTaxes(): Boolean {
        return taxesPreferences.getBoolean(PREF_INCLUDE_TAXES, false)
    }

    private fun sendLogs(fileName: String) {
        val file = Uri.fromFile(logFile)
        val storageRef: StorageReference = storageReference.child("logs/$fileName.txt")
        storageRef.putFile(file)
    }

    fun reportError(message: String) {
        val currentDate = dateFormatter.format(Date())
        database.getReference(currentDate).apply {
            child("API Level").setValue(Build.VERSION.SDK_INT)
            child("Model").setValue(Build.MODEL)
            child("Message").setValue(message)
            child("App version").setValue(BuildConfig.VERSION_NAME)
            child("App code version").setValue(BuildConfig.VERSION_CODE)
            child("Logs name").setValue(currentDate)
            collectDataFromSharedPreferences(currentDate)
            collectDataFromDB(currentDate)
            sendLogs(currentDate)
        }
    }

    private fun collectDataFromSharedPreferences(currentDate: String) {
        database.getReference(currentDate).apply {
            child(SUBSCRIPTION_PREFERENCES_NAME).child(PREF_HAS_SUBSCRIPTION).setValue(SubscriptionRepository.hasSubscription())
            child(TAXES_PREFERENCES_NAME).child(PREF_INCLUDE_TAXES).setValue(isIncludeTaxes())
        }
    }

    private fun collectDataFromDB(currentDate: String) = GlobalScope.launch {
            database.getReference(currentDate)
                .child("Data cast")
                .child("Portfolios")
                .setValue(listToMapPortfolios())
                collectSecurities(currentDate)
                collectPayments(currentDate)
                collectNews(currentDate)
            }

    private suspend fun listToMapPortfolios(): Map<String, PortfolioDbModel> { return portfolioDao.getAllPortfolios().toMap { it.name } }

    private suspend fun collectSecurities(currentDate: String) {
        for (portfolio in portfolioDao.getAllPortfolios()) {
            val securitiesMap = portfolioDao.getSecurities(portfolio.id).toMap { it.name }
            database.getReference(currentDate).child("Data cast")
                .child("Portfolios")
                .child(portfolio.name)
                .child("Securities")
                .setValue(securitiesMap)
        }
    }

    private suspend fun collectPayments(currentDate: String) {
        for (portfolio in portfolioDao.getAllPortfolios()) {
            val paymentsMap = paymentDao.getAllPaymentsWithSecurity(portfolio.id).toMap { "${it.isin} ${it.date}" }
            database.getReference(currentDate)
                .child("Data cast")
                .child("Portfolios")
                .child(portfolio.name)
                .child("Payments")
                .setValue(paymentsMap)
        }
    }

    private suspend fun collectNews(currentDate: String) {
        for (portfolio in portfolioDao.getAllPortfolios())
            for (security in securityDao.getSecurityPackagesForPortfolio(portfolio.id)) {
                val newsMap = newsDao.getPosts(listOf(security.ticker)).toMap { it.id.toString() }
                database.getReference(currentDate)
                    .child("Data cast")
                    .child("Portfolios")
                    .child(portfolio.name)
                    .child("News")
                    .setValue(newsMap)
            }
    }

    private fun <T> List<T>.toMap(getKey: (T) -> String): Map <String, T> {
        val map = mutableMapOf<String, T>()
        for (item in this)
            map[getKey(item)] = item
        return map
    }
}