package com.infinity_coder.divcalendar.data.network.gson_serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.infinity_coder.divcalendar.data.network.model.ShortStockList
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel
import java.lang.reflect.Type

class StockListDeserializer : JsonDeserializer<ShortStockList> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ShortStockList {

        val shortStockList = ShortStockList()
        if (json == null) return ShortStockList()

        val stockList = shortStockList.stocks

        val securities = json.asJsonObject.get("quotedsecurities").asJsonObject
        val dataList = securities.get("data").asJsonArray


        for (dataElement in dataList) {
            val data = dataElement.asJsonArray
            val stock = ShortStockNetworkModel(
                secid = data[1].asString,
                name = data[2].asString
            )
            stockList.add(stock)
        }

        return shortStockList
    }
}