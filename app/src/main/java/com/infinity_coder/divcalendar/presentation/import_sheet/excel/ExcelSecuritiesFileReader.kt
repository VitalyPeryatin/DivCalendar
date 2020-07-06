package com.infinity_coder.divcalendar.presentation.import_sheet.excel

import android.content.Context
import android.net.Uri
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import com.infinity_coder.divcalendar.domain.SearchInteractor
import com.infinity_coder.divcalendar.domain.SecurityInteractor
import com.infinity_coder.divcalendar.domain.models.QueryGroup
import com.infinity_coder.divcalendar.presentation.import_sheet.SecuritiesFileReader
import com.infinity_coder.divcalendar.presentation.import_sheet.SecuritiesFileReader.Companion.ERROR_CODE_EMPTY_PORTFOLIO_NAME
import com.infinity_coder.divcalendar.presentation.import_sheet.SecuritiesFileReader.Companion.ERROR_CODE_INVALID_DATA
import com.infinity_coder.divcalendar.presentation.import_sheet.SecuritiesFileReader.Companion.ERROR_CODE_NOT_UNIQUE_NAME
import kotlinx.coroutines.flow.first
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.math.BigDecimal

class ExcelSecuritiesFileReader : SecuritiesFileReader {

    private val searchInteractor = SearchInteractor()
    private val securityInteractor = SecurityInteractor()
    private val portfolioInteractor = PortfolioInteractor()

    override suspend fun readFile(context: Context, uri: Uri) {
        val fileInputStream = context.contentResolver.openInputStream(uri)
        val workbook = XSSFWorkbook(fileInputStream)
        val sheet = workbook.getSheetAt(0)

        val securities = mutableListOf<SecurityDbModel>()

        for (row in sheet.rowIterator()) {
            val ticker = row.getCell(0).stringCellValue
            val type = row.getCell(1).stringCellValue
            val market = row.getCell(2).stringCellValue
            val count = row.getCell(3).numericCellValue.toBigDecimal()
            val pricePerSecurity = row.getCell(4).numericCellValue.toBigDecimal()

            val queryGroup = QueryGroup(ticker, market, type)
            val securityNetModel = searchInteractor.search(queryGroup, limit = 1).first().firstOrNull()

            if (securityNetModel != null) {
                val security = buildSecurity(securityNetModel, count, pricePerSecurity)
                securities.add(security)
            } else {
                throw Exception(ERROR_CODE_INVALID_DATA)
            }
        }

        requestCreatePortfolio(sheet.sheetName)

        securities.forEach {
            securityInteractor.appendSecurityPackage(it)
        }
    }

    private fun buildSecurity(securityNetModel: SecurityNetModel, count: BigDecimal, price: BigDecimal): SecurityDbModel {
        return SecurityDbModel.from(securityNetModel).let {
            it.count = count
            it.totalPrice = price * count
            it.color = securityInteractor.getColorForSecurityLogo(it.logo)
            it
        }
    }

    private suspend fun requestCreatePortfolio(portfolioName: String) {
        val portfolioNames = portfolioInteractor.getAllPortfolioNames()
        when {
            portfolioName.isBlank() -> throw Exception(ERROR_CODE_EMPTY_PORTFOLIO_NAME)
            portfolioNames.contains(portfolioName) -> throw Exception(ERROR_CODE_NOT_UNIQUE_NAME)
            else -> {
                portfolioInteractor.addPortfolio(portfolioName)
                portfolioInteractor.setCurrentPortfolio(portfolioName)
            }
        }
    }
}