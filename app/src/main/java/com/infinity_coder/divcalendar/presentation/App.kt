package com.infinity_coder.divcalendar.presentation

import android.app.Application
import com.facebook.stetho.Stetho
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class App : Application() {

    private val portfolioInteractor = PortfolioInteractor()

    override fun onCreate() {
        super.onCreate()

        instance = this
        registerActivityLifecycleCallbacks(AppActivityLifecycleCallbacks())
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
        const val DEFAULT_PORTFOLIO_NAME = "Default"

        lateinit var instance: App
            private set
    }
}