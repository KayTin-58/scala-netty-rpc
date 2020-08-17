package kaytin.zhang.common.codec

import java.util

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import kaytin.zhang.common.serializer.Serializer

class RpcDecoder(clazz: Class[_], serializer: Serializer) extends ByteToMessageDecoder {

  /**
   * 解码
   *
   * @param ctx ChannelHandlerContext
   * @param in  ByteBuf
   * @param out util.List[AnyRef]
   */
  override def decode(ctx: ChannelHandlerContext, in: ByteBuf, out: util.List[AnyRef]): Unit = {
    if (in.readableBytes < 4) {
      return
    }
    in.markReaderIndex()
    val dataLength = in.readInt()
    if (in.readableBytes() < dataLength) {
      in.resetReaderIndex()
      return
    }
    val data: Array[Byte] = new Array[Byte](dataLength)
    in.readBytes(data)
    serializer.deserialize(data, clazz)
  }
}
