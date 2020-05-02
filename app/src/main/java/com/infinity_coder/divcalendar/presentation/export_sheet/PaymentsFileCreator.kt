package com.infinity_coder.divcalendar.presentation.export_sheet

import java.io.File

interface PaymentsFileCreator {
    suspend fun create(): File?
}