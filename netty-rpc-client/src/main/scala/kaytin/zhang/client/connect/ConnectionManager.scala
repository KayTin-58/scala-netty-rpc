package kaytin.zhang.client.connect

import java.net.InetSocketAddress
import java.util.concurrent.{ConcurrentHashMap, CopyOnWriteArraySet, LinkedBlockingQueue, ThreadPoolExecutor, TimeUnit}

import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import kaytin.zhang.client.handler.{RpcClientHandler, RpcClientInitializer}
import kaytin.zhang.common.loadbalance.RpcLoadBalance
import kaytin.zhang.common.pojo.RpcProtocol

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


class ConnectionManager private {
  val eventLoopGroup = new NioEventLoopGroup()
  val threadPoolExecutor = new ThreadPoolExecutor(4, 8, 600L, TimeUnit.SECONDS, new LinkedBlockingQueue[Runnable](1000))
  val serverHandlerMap = new ConcurrentHashMap[RpcProtocol, RpcClientHandler](16)
  var loadBalance: RpcLoadBalance = _
  /**
   * RpcProtocol 容器
   */
  val rpcProtocolSet = new CopyOnWriteArraySet[RpcProtocol]()

  def connectServerNode(o: RpcProtocol) = {
    val address = new InetSocketAddress(o.host, o.port)
    threadPoolExecutor.execute(() => {
      val bootstrap = new Bootstrap()
      bootstrap.group(eventLoopGroup)
        .channel(classOf[NioSocketChannel])
        .handler(new RpcClientInitializer)
    })
  }

  /**
   * setServerNode  该方法可能在同一时间点被多次出发
   *
   * @param serverNodes 最新的活性 serverNode
   */
  def setServerNodes(serverNodes: ListBuffer[RpcProtocol]): Unit = {
    if (serverNodes.nonEmpty) {
      val newServerNodes = new mutable.HashSet[RpcProtocol]()

      serverNodes.foreach(o => {
        newServerNodes.+=(o)
      })

      // remove not active serverNode
      val entry = serverHandlerMap.entrySet().iterator()
      while (entry.hasNext) {
        val item = entry.next()
        if (!newServerNodes.contains(item.getKey)) {
          entry.remove()
        } else {
          serverNodes.remove(serverNodes.indexOf(item.getKey))
        }
      }

      newServerNodes.foreach(o => {
        if (!serverHandlerMap.contains(o)) {
          // create handler
          connectServerNode(o)
        }
      })
    } else {

    }
  }

  /**
   * chose a serverNode
   *
   * @return
   */
  def choseHandler: RpcClientHandler = {

  }


}


/**
 *
 */
object ConnectionManager {
  val connectionManager: ConnectionManager = new ConnectionManager

  def getInstance: ConnectionManager = {
    connectionManager
  }
}
