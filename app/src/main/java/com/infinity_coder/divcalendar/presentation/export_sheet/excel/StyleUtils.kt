package com.infinity_coder.divcalendar.presentation.export_sheet.excel

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Workbook

fun getNormalTextCellStyle(workbook: Workbook): CellStyle {
    val normalCellStyle = workbook.createCellStyle()
    normalCellStyle.setFont(getNormalFont(workbook))
    return normalCellStyle
}

fun getNormalIntCellStyle(workbook: Workbook): CellStyle {
    val normalCellStyle = workbook.createCellStyle()
    val format = workbook.createDataFormat()
    normalCellStyle.dataFormat = format.getFormat("0")
    normalCellStyle.setFont(getNormalFont(workbook))
    return normalCellStyle
}

fun getNormalDoubleCellStyle(workbook: Workbook): CellStyle {
    val normalCellStyle = workbook.createCellStyle()
    val format = workbook.createDataFormat()
    normalCellStyle.dataFormat = format.getFormat("0.00")
    normalCellStyle.setFont(getNormalFont(workbook))
    return normalCellStyle
}

fun getHeaderCellStyle(workbook: Workbook): CellStyle {
    val headerCellStyle = workbook.createCellStyle()
    headerCellStyle.setFont(getHeaderFont(workbook))
    return headerCellStyle
}