package com.infinity_coder.divcalendar.presentation.export_sheet.excel

import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Workbook

fun getNormalFont(workbook: Workbook): Font {
    return workbook.createFont().apply {
        fontHeightInPoints = 13.toShort()
        color = IndexedColors.BLACK.getIndex()
    }
}

fun getHeaderFont(workbook: Workbook): Font {
    return workbook.createFont().apply {
        bold = true
        fontHeightInPoints = 14.toShort()
        color = IndexedColors.BLACK.getIndex()
    }
}