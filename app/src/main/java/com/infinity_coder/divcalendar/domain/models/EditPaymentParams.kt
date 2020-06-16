package com.infinity_coder.divcalendar.domain.models

data class EditPaymentParams(
    val portfolioId: Long,
    val isin: String,
    val date: String,
    val count: Int
)