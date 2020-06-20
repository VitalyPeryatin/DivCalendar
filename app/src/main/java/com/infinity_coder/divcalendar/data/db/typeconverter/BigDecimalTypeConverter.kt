package com.infinity_coder.divcalendar.data.db.typeconverter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalTypeConverter {

    @TypeConverter
    fun from(bigDecimal: BigDecimal): String {
        return bigDecimal.toPlainString()
    }

    @TypeConverter
    fun to(string: String): BigDecimal {
        return BigDecimal(string)
    }
}