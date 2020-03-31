package com.infinity_coder.divcalendar.data.network.gson_serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.infinity_coder.divcalendar.data.network.model.ShortSecList
import com.infinity_coder.divcalendar.data.network.model.ShortSecNetworkModel
import java.lang.reflect.Type

class SecListDeserializer : JsonDeserializer<ShortSecList> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ShortSecList {

        val shortSecurityList = ShortSecList()
        if (json == null) return ShortSecList()

        val securityList = shortSecurityList.securities

        val securities = json.asJsonObject.get("quotedsecurities").asJsonObject
        val dataList = securities.get("data").asJsonArray


        for (dataElement in dataList) {
            val data = dataElement.asJsonArray
            val security = ShortSecNetworkModel(
                secid = data[1].asString,
                name = data[2].asString
            )
            securityList.add(security)
        }

        return shortSecurityList
    }
}