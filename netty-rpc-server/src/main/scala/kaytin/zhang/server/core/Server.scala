package kaytin.zhang.server.core

import java.util.concurrent.ConcurrentHashMap

abstract class Server {

  /**
   * 存储服务的容器
   */
  val serverMap = new ConcurrentHashMap[String,Object]()

  /**
   * 启动服务
   */
  def start

  /**
   * 暂停服务
   */
  def stop
}
