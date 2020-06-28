package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import java.math.BigDecimal

class DefaultPortfolioInteractor {

    private val portfolioInteractor = PortfolioInteractor()
    private val securityInteractor = SecurityInteractor()

    suspend fun addDefaultPortfolio() {
        portfolioInteractor.addPortfolio(DEFAULT_PORTFOLIO_NAME)
        portfolioInteractor.setCurrentPortfolio(DEFAULT_PORTFOLIO_NAME)
        securityInteractor.appendSecurityPackage(LSRG_SECURITY_DB_MODEL)
        securityInteractor.appendSecurityPackage(MTS_SECURITY_DB_MODEL)
        securityInteractor.appendSecurityPackage(NLMK_SECURITY_DB_MODEL)
    }

    companion object {
        private const val DEFAULT_PORTFOLIO_NAME = "Основной портфель"

        private val LSRG_SECURITY_DB_MODEL = SecurityDbModel(
            isin = "RU000A0JPFP0",
            ticker = "LSRG",
            name = "Группа ЛСР",
            logo = "https://static.tinkoff.ru/brands/traiding/RU000A0JPFP0x160.png",
            exchange = "MOEX",
            count = BigDecimal(10),
            totalPrice = BigDecimal(5914.0),
            yearYield = 13.26f,
            currency = "RUB",
            type = "stock",
            color = -2090944,
            market = "russian"
        )

        private val MTS_SECURITY_DB_MODEL = SecurityDbModel(
            isin = "RU0007775219",
            ticker = "MTSS",
            name = "МТС",
            logo = "https://static.tinkoff.ru/brands/traiding/RU0007775219x160.png",
            exchange = "MOEX",
            count = BigDecimal(20),
            totalPrice = BigDecimal(6638.0),
            yearYield = 8.72f,
            currency = "RUB",
            type = "stock",
            color = -2097136,
            market = "russian"
        )

        private val NLMK_SECURITY_DB_MODEL = SecurityDbModel(
            isin = "RU0009046452",
            ticker = "NLMK",
            name = "НЛМК",
            logo = "https://static.tinkoff.ru/brands/traiding/RU0009046452x160.png",
            exchange = "MOEX",
            count = BigDecimal(40),
            totalPrice = BigDecimal(5412.0),
            yearYield = 10.32f,
            currency = "RUB",
            type = "stock",
            color = -16232288,
            market = "russian"
        )
    }
}