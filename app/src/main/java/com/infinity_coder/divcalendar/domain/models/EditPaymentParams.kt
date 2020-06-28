package com.infinity_coder.divcalendar.domain.models

import java.math.BigDecimal

data class EditPaymentParams(
    val portfolioId: Long,
    val isin: String,
    val date: String,
    val count: BigDecimal
)