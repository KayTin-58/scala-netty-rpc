package kaytin.zhang.common.utils

import java.io.IOException
import java.text.SimpleDateFormat

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.{DeserializationFeature, JavaType, ObjectMapper, SerializationFeature}

object JsonUtil {
  private val objMapper = new ObjectMapper

  val dateFormat = new SimpleDateFormat(
    "yyyy-MM-dd HH:mm:ss")
  objMapper.setDateFormat(dateFormat)
  objMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
  objMapper.enable(SerializationFeature.INDENT_OUTPUT)
  objMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false)
  objMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, false)
  objMapper.disable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)
  objMapper.disable(SerializationFeature.CLOSE_CLOSEABLE)
  objMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
  objMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
  objMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true)

  def objectToJson(o: Any): String = {
    var json = ""
    try json = objMapper.writeValueAsString(o)
    catch {
      case e: IOException =>
        throw new IllegalStateException(e.getMessage, e)
    }
    json
  }


  def jsonToObject[T](json: String, cls: Class[T]): T = {
    val javaType = objMapper.getTypeFactory.constructType(cls)
    objMapper.readValue[T](json,javaType)
  }

}
