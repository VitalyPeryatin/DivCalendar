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
import com.infinity_coder.divcalendar.data.db.model.NewsPostDbModel
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
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

    val portfolioDao = DivCalendarDatabase.roomDatabase.portfolioDao

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

    private fun collectDataFromDB(currentDate: String) {
        GlobalScope.launch {
            database.getReference(currentDate).child("Data cast")
                .child("Portfolios").setValue(collectPortfolios())
                collectSecurities(currentDate)
                collectPayments(currentDate)
                collectNews(currentDate)
            }
        }

    private suspend fun collectPortfolios(): Map<String, PortfolioDbModel> {
        val portfolioMap = mutableMapOf<String, PortfolioDbModel>()
        for (portfolio in portfolioDao.getAllPortfolios()) {
            portfolioMap[portfolio.name] = portfolio
        }
        return portfolioMap
    }

    private suspend fun collectSecurities(currentDate: String) {
        for (portfolio in portfolioDao.getAllPortfolios()) {
            database.getReference(currentDate).child("Data cast")
                .child("Portfolios")
                .child(portfolio.name)
                .child("Securities")
                .setValue(listToMapSecurity(portfolioDao.getSecurities(portfolio.id)))
        }
    }

    private suspend fun collectPayments(currentDate: String) {
        val paymentDao = DivCalendarDatabase.roomDatabase.paymentDao
        for (portfolio in portfolioDao.getAllPortfolios()) {
            database.getReference(currentDate).child("Data cast")
                .child("Portfolios")
                .child(portfolio.name)
                .child("Payments")
                .setValue(listToMapPayments(paymentDao.getAllPaymentsWithSecurity(portfolio.id)))
            }
    }

    private suspend fun collectNews(currentDate: String) {
        val newsDao = DivCalendarDatabase.roomDatabase.newsDao
        val securityDao = DivCalendarDatabase.roomDatabase.securityDao
        for (portfolio in portfolioDao.getAllPortfolios())
            for (security in securityDao.getSecurityPackagesForPortfolio(portfolio.id)) {
                database.getReference(currentDate).child("Data cast")
                    .child("Portfolios")
                    .child(portfolio.name)
                    .child("News")
                    .setValue(listToMapNews(newsDao.getPosts(listOf(security.ticker))))
            }
    }

    private fun listToMapSecurity(securityList: List<SecurityDbModel>): Map<String, SecurityDbModel> {
        val securityMap = mutableMapOf<String, SecurityDbModel>()
        for (security in securityList)
            securityMap[security.name] = security
        return securityMap
    }

    private fun listToMapNews(allNews: List<NewsPostDbModel>): Map<String, NewsPostDbModel> {
        val newsMap = mutableMapOf<String, NewsPostDbModel>()
        for (news in allNews)
            newsMap[(news.id).toString()] = news
        return newsMap
    }

    private fun listToMapPayments(payments: List<PaymentDbModel>): Map<String, PaymentDbModel> {
        val paymentsMap = mutableMapOf<String, PaymentDbModel>()
        for (payment in payments)
            paymentsMap["${payment.isin} ${payment.date}"] = payment
        return paymentsMap
    }
}