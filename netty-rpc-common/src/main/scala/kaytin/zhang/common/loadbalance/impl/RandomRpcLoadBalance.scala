package kaytin.zhang.common.loadbalance.impl

import kaytin.zhang.common.RpcException
import kaytin.zhang.common.loadbalance.RpcLoadBalance
import kaytin.zhang.common.pojo.RpcProtocol
import org.jboss.netty.channel.SimpleChannelHandler

import scala.util.Random

class RandomRpcLoadBalance extends RpcLoadBalance {

  val random = new Random()

  /**
   * RandomRpcLoadBalance
   *
   * @param serviceKey serviceKey
   * @return
   */
  override def routine(serviceKey: String, serverNodes: Map[RpcProtocol, SimpleChannelHandler]): SimpleChannelHandler = {
    if (_serverNodes.contains(serviceKey)) {
      val rpcRpcProtocols = _serverNodes.get(serviceKey)
      if (rpcRpcProtocols.nonEmpty) {
        val size = rpcRpcProtocols.size
        val a = rpcRpcProtocols(random.nextInt(size))
        serverNodes(a)
      } else {
        throw new RpcException(-1, "not find ")
      }
    } else {
      throw new RpcException(-1, "not find ")
    }
  }
}
