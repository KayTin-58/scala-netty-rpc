package kaytin.zhang.common.pojo
import kaytin.zhang.common.utils.JsonUtil
import scala.util.Random

class RpcProtocol {

  val serialVersionUID = -1102180003395190700L
  var host: String = _
  var port: Int = _
  var serviceName: String = _
  var version: String = _

  /**
   * this to json
   *
   * @return
   */
  def toJson: String = {
    JsonUtil.objectToJson(this)
  }

  override def equals(o: Any): Boolean = {
    if (this == o) return true
    if (o == null || (getClass ne o.getClass)) return false
    val that = o.asInstanceOf[RpcProtocol]
    port == that.port && host == that.host && serviceName == that.serviceName && version == that.version
  }

  override def hashCode(): Int = {
    new Random().nextInt(Integer.MAX_VALUE) + System.currentTimeMillis().toInt
  }
}
