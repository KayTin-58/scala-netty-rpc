package kaytin.zhang.common.loadbalance

import java.util.concurrent.ConcurrentHashMap

import kaytin.zhang.common.pojo.RpcProtocol
import kaytin.zhang.common.utils.ServerUtil
import org.jboss.netty.channel.SimpleChannelHandler
import scala.collection.mutable.ListBuffer

trait RpcLoadBalance {
  /**
   * serverKey
   * List[RpcProtocol]
   */
  var _serverNodes = new ConcurrentHashMap[String, ListBuffer[RpcProtocol]]()

  /**
   * 初始化serverKey 和 RpcProtocol的映射，存在一对多的关系
   *
   * @param serverNodes
   */
  def initServerNodes(serverNodes: Map[RpcProtocol, SimpleChannelHandler]): Unit = {
    for (k <- serverNodes.keySet) {
      val serverKey = ServerUtil.makeServiceKey(k.serviceName, k.version)
      if (_serverNodes.contains(serverKey)) {
        _serverNodes.get(serverKey).+=(k)
      } else {
        _serverNodes.put(serverKey, new ListBuffer[RpcProtocol]().+=(k))
      }
    }
  }

  /**
   *
   * @param serviceKey
   * @return
   */
  def routine(serviceKey: String, serverNodes: Map[RpcProtocol, SimpleChannelHandler]): SimpleChannelHandler
}
