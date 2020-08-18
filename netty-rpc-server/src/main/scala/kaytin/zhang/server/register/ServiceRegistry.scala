package kaytin.zhang.server.register

import java.util.concurrent.ConcurrentHashMap

import kaytin.zhang.common.constants.Constants
import kaytin.zhang.common.pojo.{Address, RpcProtocol}
import kaytin.zhang.common.utils.ServerUtil
import kaytin.zhang.common.zkclient.CuratorClient
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.state.ConnectionState

import scala.collection.mutable
/**
 * register server to zk
 */
class ServiceRegistry {
  var curatorClient: CuratorClient = _
  val pathList = new mutable.ListBuffer[String]()

  def this(registerAddress: Address) {
    this()
    curatorClient = new CuratorClient(registerAddress.toString, 5000)
  }

  /**
   * registerServer
   *
   * @param address   address
   * @param serverMap serverMap
   */
  def registerServer(address: Address, serverMap: ConcurrentHashMap[String, Any]): Unit = {
    val ele = serverMap.keys()
    while (ele.hasMoreElements) {
      val key = ele.nextElement()
      val rpcProtocol = new RpcProtocol
      rpcProtocol.host = address.host
      rpcProtocol.port = address.port
      val serviceInfo: Array[String] = key.split(ServerUtil.SERVICE_CONCAT_TOKEN)
      if (serviceInfo.length > 0) {
        rpcProtocol.serviceName = serviceInfo(0);
      } else if (serviceInfo.length == 2) {
        rpcProtocol.version = serviceInfo(1)
      } else {
        rpcProtocol.version = ""
      }
      val data = RpcProtocol.toJson
      val bytes = data.getBytes
      val path = Constants.ZK_DATA_PATH + rpcProtocol.hashCode()
      curatorClient.createPathData(path, bytes)
      pathList += path
    }
    curatorClient.addConnectionStateListener((curatorFramework: CuratorFramework, connectionState: ConnectionState) => {
      if (connectionState == ConnectionState.RECONNECTED) {
        registerServer(address, serverMap)
      }
    })
  }

  def unregisterAllServer = {
    for (item <- pathList) {
      curatorClient.deletePath(item)
    }
    curatorClient.close()
  }
}
