package com.infinity_coder.divcalendar.data.db.typeconverter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalTypeConverter {

    @TypeConverter
    fun from(dividends: BigDecimal): String {
        return dividends.toString()
    }

    @TypeConverter
    fun to(dividends: String): BigDecimal {
        return BigDecimal(dividends)
    }
}