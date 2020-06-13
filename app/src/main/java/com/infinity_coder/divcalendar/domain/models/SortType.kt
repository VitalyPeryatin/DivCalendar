package com.infinity_coder.divcalendar.domain.models

import java.lang.IllegalStateException

enum class SortType {
    PAYMENT_DATE, PROFITABILITY, ALPHABETICALLY;

    companion object {

        fun getSortTypeByName(name: String): SortType {
            return when (name) {
                PAYMENT_DATE.name -> PAYMENT_DATE

                PROFITABILITY.name -> PROFITABILITY

                ALPHABETICALLY.name -> ALPHABETICALLY

                else -> throw IllegalStateException("No type sort for such name: $name")
            }
        }

    }
}