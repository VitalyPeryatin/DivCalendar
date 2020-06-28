package com.infinity_coder.divcalendar.data.db.typeconverter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimaltypeConverter {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): String {
        return value.toPlainString()
    }

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal {
        return BigDecimal(value)
    }
}