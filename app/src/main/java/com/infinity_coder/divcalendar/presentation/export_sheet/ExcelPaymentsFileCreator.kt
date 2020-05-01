package com.infinity_coder.divcalendar.presentation.export_sheet

import android.content.Context
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.domain.PaymentInteractor
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class ExcelPaymentsFileCreator(private val context: Context) : PaymentsFileCreator {

    private val portfolioInteractor = PortfolioInteractor()
    private val paymentInteractor = PaymentInteractor()

    private val portfolioColumnsTitles = arrayOf(
        "ISIN", "Название", "Тикер", "Тип", "Биржа", "Логотип", "Годовая доходность", "Валюта", "Количество", "Сумма вложений"
    )

    private val paymentsColumnsTitles = arrayOf(
        "ISIN", "Эмитент", "Количество", "Утверждено", "Дата выплаты", "Выплата", "Валюта"
    )

    override suspend fun create(): File? {
        val files = arrayListOf<File>()
        val portfolios = portfolioInteractor.getAllPortfoliosWithSecurities()

        for (portfolio in portfolios) {
            val workbook: Workbook = XSSFWorkbook()

            val portfolioSheet = workbook.createSheet("Портфель")

            val headerFont: Font = workbook.createFont().apply {
                bold = true
                fontHeightInPoints = 14.toShort()
                color = IndexedColors.BLACK.getIndex()
            }
            val headerCellStyle = workbook.createCellStyle()
            headerCellStyle.setFont(headerFont)

            val headerPortfolioRow = portfolioSheet.createRow(0)

            val portfolioColumnWidthMap = hashMapOf(
                0 to 3600,
                1 to 2000,
                2 to 2000,
                3 to 3600,
                4 to 3600,
                5 to 3600,
                6 to 5000,
                7 to 2000,
                8 to 3600,
                9 to 5000
            )

            portfolioColumnsTitles.forEachIndexed { index, column ->
                val cell = headerPortfolioRow.createCell(index)
                cell.setCellValue(column)
                cell.cellStyle = headerCellStyle

                portfolioSheet.setColumnWidth(index, portfolioColumnWidthMap[index] ?: 0)
            }

            portfolio.securities.forEachIndexed { index, security ->
                val row = portfolioSheet.createRow(index + 1)
                row.createCell(0).setCellValue(security.isin)
                row.createCell(1).setCellValue(security.name)
                row.createCell(2).setCellValue(security.ticker)
                row.createCell(3).setCellValue(security.type)
                row.createCell(4).setCellValue(security.exchange)
                row.createCell(5).setCellValue(security.logo)
                row.createCell(6).setCellValue(security.yearYield.toString())
                row.createCell(7).setCellValue(security.currency)
                row.createCell(8).setCellValue(security.count.toString())
                row.createCell(9).setCellValue(security.totalPrice)
            }

            val createHelper = workbook.creationHelper
            val dateCellStyle = workbook.createCellStyle()
            dateCellStyle.dataFormat = createHelper.createDataFormat().getFormat("dd-MM-yyyy")

            val paymentsSheet = workbook.createSheet("Выплаты")

            val headerPaymentsRow = paymentsSheet.createRow(0)

            val paymentsColumnWidthMap = hashMapOf(
                0 to 3600,
                1 to 3600,
                2 to 3600,
                3 to 3600,
                4 to 3600,
                5 to 3600,
                6 to 3600
            )

            paymentsColumnsTitles.forEachIndexed { index, column ->
                val cell = headerPaymentsRow.createCell(index)
                cell.setCellValue(column)
                cell.cellStyle = headerCellStyle

                portfolioSheet.setColumnWidth(index, paymentsColumnWidthMap[index] ?: 0)
            }

            val payments = paymentInteractor.getAllPayments(portfolio.id)

            payments.forEachIndexed { index, payment ->

                val securityCount = (payment.security?.count ?: 0).toString()
                val securityCountString = context.resources.getString(R.string.sec_count, securityCount)
                val isApproved = if (payment.forecast) "Нет" else "Да"

                val row = paymentsSheet.createRow(index + 1)
                row.createCell(0).setCellValue(payment.isin)
                row.createCell(1).setCellValue(payment.security?.name)
                row.createCell(2).setCellValue(securityCountString)
                row.createCell(3).setCellValue(isApproved)

                val dateCell = row.createCell(4)
                dateCell.cellStyle = dateCellStyle
                dateCell.setCellValue(payment.date)

                row.createCell(5).setCellValue(payment.dividends)
                row.createCell(6).setCellValue(payment.security?.currency)
            }

            val file = File(context.filesDir, "${portfolio.name}.xlsx")
            withContext(Dispatchers.IO) {
                if (file.exists()) {
                    file.delete()
                }
                if (file.createNewFile()) {
                    val fileOut = FileOutputStream(file)
                    workbook.write(fileOut)
                    fileOut.close()
                }
                workbook.close()
            }
            files.add(file)
        }
        return if (files.size > 1) {
            ZipManager.zip(files, "Security payments.zip")
        } else {
            files[0]
        }
    }
}