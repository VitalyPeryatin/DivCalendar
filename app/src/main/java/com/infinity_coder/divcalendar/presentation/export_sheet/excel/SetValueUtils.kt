package com.infinity_coder.divcalendar.presentation.export_sheet.excel

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import java.util.*

fun Row.setValueAtCell(workbook: Workbook, index: Int, value: String?) {
    createCell(index).apply {
        cellStyle = getNormalTextCellStyle(workbook)
        setCellValue(value)
    }
}

fun Row.setValueAtHeaderCell(workbook: Workbook, index: Int, value: String?) {
    createCell(index).apply {
        cellStyle = getHeaderCellStyle(workbook)
        setCellValue(value)
    }
}

fun Row.setValueAtCell(workbook: Workbook, index: Int, value: Int?) {
    createCell(index).apply {
        cellStyle = getNormalIntCellStyle(workbook)
        setCellValue((value ?: 0).toDouble())
    }
}

fun Row.setValueAtCell(workbook: Workbook, index: Int, value: Double?) {
    createCell(index).apply {
        cellStyle = getNormalDoubleCellStyle(workbook)
        setCellValue(value ?: 0.0)
    }
}

fun Row.setValueAtCell(workbook: Workbook, index: Int, value: Date?) {
    val createHelper = workbook.creationHelper
    val dateCellStyle = getNormalTextCellStyle(workbook)
    dateCellStyle.dataFormat = createHelper.createDataFormat().getFormat("dd-MM-yyyy")
    createCell(index).apply {
        cellStyle = dateCellStyle
        setCellValue(value)
    }
}

fun Row.setPercentValueAtCell(workbook: Workbook, index: Int, value: Double?) {
    val format = workbook.createDataFormat()
    val percentCellStyle = getNormalTextCellStyle(workbook)
    percentCellStyle.dataFormat = format.getFormat("0.00%")
    createCell(index).apply {
        cellStyle = percentCellStyle
        setCellValue(value ?: 0.0)
    }
}