package com.infinity_coder.divcalendar.presentation.export_sheet.excel

import android.content.Context
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.domain.PaymentInteractor
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import com.infinity_coder.divcalendar.domain._common.convertStingToDate
import com.infinity_coder.divcalendar.presentation._common.SecurityTypeDelegate
import com.infinity_coder.divcalendar.presentation.export_sheet.PaymentsFileCreator
import com.infinity_coder.divcalendar.presentation.export_sheet.ZipManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class ExcelPaymentsFileCreator(private val context: Context) : PaymentsFileCreator {

    private val resources = context.resources

    private val portfolioInteractor = PortfolioInteractor()
    private val paymentInteractor = PaymentInteractor()

    override suspend fun create(): File? {
        val files = arrayListOf<File>()
        val portfolios = portfolioInteractor.getAllPortfoliosWithSecurities()

        for (portfolio in portfolios) {
            val workbook: Workbook = XSSFWorkbook()

            addPortfolioSheet(workbook, portfolio)
            addPaymentsSheet(workbook, portfolio)

            saveWorkbook(workbook, files, portfolio.name)

        }
        return getPackedFile(files)
    }

    private fun addPortfolioSheet(workbook: Workbook, portfolio: PortfolioDbModel) {

        val portfolioTitleParams = hashMapOf(
            0 to HeaderCellParams(
                value = resources.getString(R.string.title_portfolio_value_isin),
                width = 4500
            ),
            1 to HeaderCellParams(
                value = resources.getString(R.string.title_portfolio_value_name),
                width = 4000
            ),
            2 to HeaderCellParams(
                value = resources.getString(R.string.title_portfolio_value_ticker),
                width = 2000
            ),
            3 to HeaderCellParams(
                value = resources.getString(R.string.title_portfolio_value_type),
                width = 3000
            ),
            4 to HeaderCellParams(
                value = resources.getString(R.string.title_portfolio_value_exchange),
                width = 3600
            ),
            5 to HeaderCellParams(
                value = resources.getString(R.string.title_portfolio_value_logo),
                width = 3600
            ),
            6 to HeaderCellParams(
                value = resources.getString(R.string.title_portfolio_value_annual_yield),
                width = 6000
            ),
            7 to HeaderCellParams(
                value = resources.getString(R.string.title_portfolio_value_count),
                width = 3600
            ),
            8 to HeaderCellParams(
                value = resources.getString(R.string.title_portfolio_value_total_price),
                width = 5000
            ),
            9 to HeaderCellParams(
                value = resources.getString(R.string.title_portfolio_value_currency),
                width = 2400
            )
        )

        val portfolioTitle = resources.getString(R.string.title_sheet_portfolio)
        val portfolioSheet = workbook.createSheet(portfolioTitle)

        val headerPortfolioRow = portfolioSheet.createRow(0)

        val portfolioTitles = portfolioTitleParams.values.map { it.value }
        portfolioTitles.forEachIndexed { index, title ->
            headerPortfolioRow.setValueAtHeaderCell(workbook, index, title)

            portfolioSheet.setColumnWidth(index, portfolioTitleParams[index]?.width ?: 0)
        }

        portfolio.securities.forEachIndexed { index, security ->
            val type = when (security.type) {
                SecurityTypeDelegate.SECURITY_TYPE_STOCK -> resources.getString(R.string.cell_value_stock)
                SecurityTypeDelegate.SECURITY_TYPE_BOND-> resources.getString(R.string.cell_value_bond)
                else -> "-"
            }

            portfolioSheet.createRow(index + 1).apply {
                setValueAtCell(workbook, 0, security.isin)
                setValueAtCell(workbook, 1, security.name)
                setValueAtCell(workbook, 2, security.ticker)
                setValueAtCell(workbook, 3, type)
                setValueAtCell(workbook, 4, security.exchange)
                setValueAtCell(workbook, 5, security.logo)
                setPercentValueAtCell(workbook, 6, security.yearYield / 100.0)
                setValueAtCell(workbook, 7, security.count)
                setValueAtCell(workbook, 8, security.totalPrice)
                setValueAtCell(workbook, 9, security.currency)
            }
        }
    }

    private suspend fun addPaymentsSheet(workbook: Workbook, portfolio: PortfolioDbModel) {

        val paymentTitleParams = hashMapOf(
            0 to HeaderCellParams(
                value = resources.getString(R.string.title_payments_value_isin),
                width = 4500
            ),
            1 to HeaderCellParams(
                value = resources.getString(R.string.title_payments_value_security),
                width = 4000
            ),
            2 to HeaderCellParams(
                value = resources.getString(R.string.title_payments_value_count),
                width = 3600
            ),
            3 to HeaderCellParams(
                value = resources.getString(R.string.title_payments_value_approved),
                width = 3600
            ),
            4 to HeaderCellParams(
                value = resources.getString(R.string.title_payments_value_data),
                width = 4000
            ),
            5 to HeaderCellParams(
                value = resources.getString(R.string.title_payments_value_diviens),
                width = 3600
            ),
            6 to HeaderCellParams(
                value = resources.getString(R.string.title_payments_value_currency),
                width = 3600
            )
        )

        val paymentsTitleSheet = resources.getString(R.string.title_sheet_payments)
        val paymentsSheet = workbook.createSheet(paymentsTitleSheet)
        val headerPaymentsRow = paymentsSheet.createRow(0)

        val paymentsTitles = paymentTitleParams.values.map { it.value }
        paymentsTitles.forEachIndexed { index, title ->
            headerPaymentsRow.setValueAtHeaderCell(workbook, index, title)

            paymentsSheet.setColumnWidth(index, paymentTitleParams[index]?.width ?: 0)
        }

        val payments = paymentInteractor.getAllPayments(portfolio.id)

        payments.forEachIndexed { index, payment ->
            val isApproved = if (payment.forecast) resources.getString(R.string.cell_value_no) else resources.getString(R.string.cell_value_yes)

            paymentsSheet.createRow(index + 1).apply {
                setValueAtCell(workbook, 0, payment.isin)
                setValueAtCell(workbook, 1, payment.security?.name)
                setValueAtCell(workbook, 2, payment.security?.count ?: 0)
                setValueAtCell(workbook, 3, isApproved)
                setValueAtCell(workbook, 4, convertStingToDate(payment.date))
                setValueAtCell(workbook, 5, payment.dividends)
                setValueAtCell(workbook, 6, payment.security?.currency)
            }
        }
    }

    private suspend fun saveWorkbook(workbook: Workbook, files: MutableList<File>, fileName: String) {
        withContext(Dispatchers.IO) {
            val file = File(context.filesDir, "$fileName.xlsx")
            if (file.exists()) {
                file.delete()
            }
            if (file.createNewFile()) {
                val fileOut = FileOutputStream(file)
                workbook.write(fileOut)
                fileOut.close()
            }
            workbook.close()
            files.add(file)
        }
    }

    private fun getPackedFile(files: List<File>): File? {
        val fileName = resources.getString(R.string.final_file_name)
        return when {
            files.size > 1 -> {
                ZipManager.zip(
                    files,
                    "$fileName.zip"
                )
            }
            files.size == 1 -> {
                files[0]
            }
            else -> null
        }
    }

    private data class HeaderCellParams(
        val value: String = "",
        val width: Int = 0
    )
}