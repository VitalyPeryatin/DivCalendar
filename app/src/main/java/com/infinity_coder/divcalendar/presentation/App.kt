package com.infinity_coder.divcalendar.presentation

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import com.infinity_coder.divcalendar.domain._common.Actualizer
import com.infinity_coder.divcalendar.presentation._common.clearLogFile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class App : MultiDexApplication() {

    private val portfolioInteractor = PortfolioInteractor()

    override fun onCreate() {
        super.onCreate()

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl")

        instance = this
        context = applicationContext
        registerActivityLifecycleCallbacks(AppActivityLifecycleCallbacks())
        initActualizer()
        clearLogFile()
    }

    private fun initActualizer() {
        Actualizer.subscribe(RateRepository::updateRates, RATE_OUT_DATE_LIMIT)
        Stetho.initializeWithDefaults(this)

        addDefaultPortfolio()
    }

    private fun addDefaultPortfolio() = GlobalScope.launch {
        if (portfolioInteractor.getCurrentPortfolioName().isEmpty()) {
            portfolioInteractor.addPortfolio(DEFAULT_PORTFOLIO_NAME)
            portfolioInteractor.setCurrentPortfolio(DEFAULT_PORTFOLIO_NAME)
        }
    }

    companion object {
        const val DEFAULT_PORTFOLIO_NAME = "Основной портфель"

        private const val RATE_OUT_DATE_LIMIT = 30 * 60 * 1000L

        lateinit var instance: App
            private set
        lateinit var context: Context
            private set
    }
}