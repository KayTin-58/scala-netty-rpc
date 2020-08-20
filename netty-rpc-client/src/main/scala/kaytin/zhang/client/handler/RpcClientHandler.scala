package kaytin.zhang.client.handler

import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}

class RpcClientHandler extends SimpleChannelInboundHandler{
  override def channelRead0(ctx: ChannelHandlerContext, msg: Nothing): Unit = ???
}
