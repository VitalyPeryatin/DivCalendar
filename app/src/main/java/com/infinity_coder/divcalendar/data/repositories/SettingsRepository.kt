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
import com.infinity_coder.divcalendar.presentation.App
import com.infinity_coder.divcalendar.presentation._common.logFile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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
            sendLogs(currentDate)
        }
    }

    fun sendDataCast() {
        val currentDate = dateFormatter.format(Date())
        database.getReference(currentDate).apply {
            child("API Level").setValue(Build.VERSION.SDK_INT)
            child("Model").setValue(Build.MODEL)
            child("App version").setValue(BuildConfig.VERSION_NAME)
            child("App code version").setValue(BuildConfig.VERSION_CODE)
            collectDataFromSharedPreferences(currentDate)
            collectDataFromDB(currentDate)
        }
    }

    private fun collectDataFromSharedPreferences(currentDate: String) {
        database.getReference(currentDate).apply {
            child(SUBSCRIPTION_PREFERENCES_NAME).child(PREF_HAS_SUBSCRIPTION).setValue(SubscriptionRepository.hasSubscription())
            child(TAXES_PREFERENCES_NAME).child(PREF_INCLUDE_TAXES).setValue(isIncludeTaxes())
        }
    }

    private fun collectDataFromDB(currentDate: String) {
        val portfolioDao = DivCalendarDatabase.roomDatabase.portfolioDao
        val newsDao = DivCalendarDatabase.roomDatabase.newsDao
        val paymentDao = DivCalendarDatabase.roomDatabase.paymentDao
        GlobalScope.launch{
            database.getReference(currentDate).apply {
                child("Database").child("Portfolios").setValue(listToMapPortfolios(portfolioDao.getPortfolio()))
                child("Database").child("News").setValue(listToMapNews(newsDao.getAllNews()))
                child("Database").child("Payments").setValue(listToMapPayments(paymentDao.getAllPayments()))
            }
        }
    }

    private fun listToMapPortfolios(portfolioList : List<PortfolioDbModel>) : Map<String, PortfolioDbModel> {
        val portfolioMap : MutableMap<String, PortfolioDbModel> = HashMap()
        for (portfolio in portfolioList)
            portfolioMap[(portfolio.id).toString()] = portfolio
        return portfolioMap
    }

    private fun listToMapNews(newsList : List<NewsPostDbModel>) : Map<String, NewsPostDbModel> {
        val newsMap: MutableMap<String, NewsPostDbModel> = HashMap()
        for (news: NewsPostDbModel in newsList)
            newsMap[(news.id).toString()] = news
        return newsMap
    }

    private fun listToMapPayments(paymentsList: List<PaymentDbModel>) : Map<String, PaymentDbModel> {
        val paymentsMap: MutableMap<String, PaymentDbModel> = HashMap()
        for (payment: PaymentDbModel in paymentsList)
            paymentsMap[(payment.portfolioId).toString()] = payment
        return paymentsMap
    }
}