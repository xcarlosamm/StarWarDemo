package co.swapi.demo.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type


class UrlDeserializer: JsonDeserializer<Any> {

    override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): Any {
        return  if (json.isJsonPrimitive) parse(json.asString) else json.asJsonArray.map { parse(it.asString) }
    }

    fun parse(url: String): Long {
        return "\\d+".toRegex().findAll(url).last().value.toLong()
    }
}
