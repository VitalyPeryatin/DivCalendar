package com.infinity_coder.divcalendar.data.db.typeconverter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalTypeConverter {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return when (value) {
            null -> null
            else ->value.toPlainString()
        }
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return when (value) {
            null -> null
            else -> BigDecimal(value)
        }
    }
}