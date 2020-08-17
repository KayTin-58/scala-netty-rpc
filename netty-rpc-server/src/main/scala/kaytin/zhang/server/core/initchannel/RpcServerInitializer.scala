package kaytin.zhang.server.core.initchannel

import java.util.concurrent.{ConcurrentHashMap, ThreadPoolExecutor, TimeUnit}

import io.netty.channel.{Channel, ChannelInitializer, ChannelPipeline}
import kaytin.zhang.common.serializer.Serializer

class RpcServerInitializer(handlerMap: ConcurrentHashMap[String, Object], threadPoolExecutor: ThreadPoolExecutor,
                           serializer: Serializer)
  extends ChannelInitializer[Channel] {

  /**
   * initChannel
   *
   * @param ch ch
   */
  override def initChannel(ch: Channel): Unit = {
    val cp = ch.pipeline
    cp.addLast()
  }
}
