package kaytin.zhang.server.core

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.{ChannelOption}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import kaytin.zhang.common.utils.ServerUtil
import kaytin.zhang.common.pojo.{Address, ServerPo}
import kaytin.zhang.server.register.ServiceRegistry

class NettyServer(serverAddress: Address) extends Server {

  var _thread: Thread = _
  var serviceRegistry: ServiceRegistry = _

  def this(serverAddress: Address, register: Address) {
    this(serverAddress)
    serviceRegistry = new ServiceRegistry(register)
  }


  /**
   * start server
   */
  override def start: Unit = {
    _thread = new Thread(() => {
      val bossGroup = new NioEventLoopGroup()
      val workGroup = new NioEventLoopGroup()
      try {
        val bootstrap = new ServerBootstrap()
        bootstrap.group(bossGroup, workGroup)
          .channel(classOf[NioServerSocketChannel])
          .option(ChannelOption.SO_BACKLOG, Integer.valueOf(128))
          .childOption(ChannelOption.SO_KEEPALIVE, true.asInstanceOf[java.lang.Boolean])
        val future = bootstrap.bind(serverAddress.host, serverAddress.port).sync

        /**
         * register server to zk
         */
        serviceRegistry.registerServer(serverAddress,serverMap)
        future.channel().closeFuture().sync()
      } catch {
        case e: Exception => {
          if (e.isInstanceOf[InterruptedException]) {
          } else {
          }
        }
      } finally {
        /**
         * server unregister
         * close netty
         */
        serviceRegistry.unregisterAllServer
        workGroup.shutdownGracefully()
        bossGroup.shutdownGracefully()
      }
    })
    _thread.start()
  }

  /**
   * stop server
   */
  override def stop: Unit = {
    if (_thread != null && _thread.isAlive) {
      _thread.interrupt()
    }
  }

  /**
   * addServer
   *
   * @param serverPo serverPo
   */
  def addServer(serverPo: ServerPo): Unit = {
    val serverKey = ServerUtil.makeServiceKey(serverPo.interfaceName, serverPo.version)
    serverMap.put(serverKey, serverPo.serverBean)
  }
}

object NettyServer {
  def apply(serverAddress: Address, registerAddress: Address): NettyServer = {
    new NettyServer(serverAddress, registerAddress)

  }
}
