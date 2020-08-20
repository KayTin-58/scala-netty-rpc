package kaytin.zhang.client.handler


import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

class RpcClientInitializer extends ChannelInitializer[SocketChannel]{
  override def initChannel(ch: SocketChannel): Unit = ???
}
