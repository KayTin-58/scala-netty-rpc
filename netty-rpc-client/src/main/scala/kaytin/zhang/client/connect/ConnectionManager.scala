package kaytin.zhang.client.connect

import java.net.InetSocketAddress
import java.util.concurrent.{ConcurrentHashMap, CopyOnWriteArraySet, LinkedBlockingQueue, ThreadPoolExecutor, TimeUnit}

import kaytin.zhang.common.pojo.RpcProtocol
import org.jboss.netty.channel.SimpleChannelHandler

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


class ConnectionManager private {

  val threadPoolExecutor = new ThreadPoolExecutor(4, 8, 600L, TimeUnit.SECONDS, new LinkedBlockingQueue[Runnable](1000))
  val serverHandlerMap = new ConcurrentHashMap[RpcProtocol,SimpleChannelHandler](16)
  /**
   * RpcProtocol 容器
   */
  val copyOnWriteArraySet = new CopyOnWriteArraySet[RpcProtocol]()

  def creatServerHandler(o: RpcProtocol) = {
    val address = new InetSocketAddress(o.host, o.port)
    threadPoolExecutor.execute(() => {

    })
  }

  /**
   * set
   * @param serverNodes 最新的活性 serverNode
   */
  def setServerNodes(serverNodes: ListBuffer[RpcProtocol]): Unit = {
    if(serverNodes.nonEmpty) {
      val set = new mutable.HashSet[RpcProtocol]()
      // 去重
      serverNodes.foreach(o => {
        set.+=(o)
      })

      // 根据rpc 创建 ChannelHandler
      set.foreach(o => {
        if(!copyOnWriteArraySet.contains(o)) {
          copyOnWriteArraySet.add(o)
          creatServerHandler(o)
        }
      })

      // 删除 ChannelHandler
      copyOnWriteArraySet.forEach(o => {
        if(!set.contains(o)) {
          val tempHandler = serverHandlerMap.get(o)
          if(tempHandler != null) {

          }
          serverHandlerMap.remove(o)
        }
      })


    }
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
