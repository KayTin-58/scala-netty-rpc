package kaytin.zhang.common.utils

object ServerUtil {
  val SERVICE_CONCAT_TOKEN = "#"

  /**
   * makeServiceKey
   *
   * @param interfaceName interfaceName
   * @param version       version
   * @return
   */
  def makeServiceKey(interfaceName: String, version: String): String = {
    var serviceKey = interfaceName
    if (version != null && version.trim.length > 0) {
      serviceKey = serviceKey + SERVICE_CONCAT_TOKEN.concat(version)
    }
    serviceKey
  }
}
