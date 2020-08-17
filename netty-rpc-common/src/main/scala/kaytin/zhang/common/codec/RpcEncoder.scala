package kaytin.zhang.common.codec

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import kaytin.zhang.common.serializer.Serializer

class RpcEncoder(clazz: Class[_], serializer: Serializer) extends MessageToByteEncoder {

  /**
   * 编码
   *
   * @param ctx ChannelHandlerContext
   * @param msg Nothing
   * @param out ByteBuf
   */
  override def encode(ctx: ChannelHandlerContext, msg: Nothing, out: ByteBuf): Unit = {
    if (clazz.isInstance(msg)) try {
      val data = serializer.serialize(msg)
      out.writeInt(data.length)
      out.writeBytes(data)
    } catch {
      case ex: Exception => println(ex.toString)
    }
  }
}
