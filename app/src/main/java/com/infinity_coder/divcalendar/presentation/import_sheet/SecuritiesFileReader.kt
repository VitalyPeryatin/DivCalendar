package com.infinity_coder.divcalendar.presentation.import_sheet

import android.content.Context
import android.net.Uri

interface SecuritiesFileReader {
    suspend fun readFile(context: Context, uri: Uri)

    companion object {
        const val ERROR_CODE_INVALID_DATA = "INVALID_DATA"
        const val ERROR_CODE_EMPTY_PORTFOLIO_NAME = "EMPTY_PORTFOLIO_NAME"
        const val ERROR_CODE_NOT_UNIQUE_NAME = "NOT_UNIQUE_NAME"
    }
}