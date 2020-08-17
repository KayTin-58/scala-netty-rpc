package kaytin.zhang.server.core.handler

import java.util.concurrent.{ConcurrentHashMap, ThreadPoolExecutor}

import io.netty.channel.{ChannelFuture, ChannelHandlerContext, SimpleChannelInboundHandler}
import io.netty.handler.timeout.IdleStateEvent
import kaytin.zhang.common.codec.{RpcRequest, RpcResponse}
import kaytin.zhang.common.utils.ServerUtil
import net.sf.cglib.reflect.FastClass

class RpcServerHandler extends SimpleChannelInboundHandler[RpcRequest] {

  var _serverMap: ConcurrentHashMap[String, Object] = _
  var _threadPoolExe: ThreadPoolExecutor = _

  def this(serverMap: ConcurrentHashMap[String, Object], threadPoolExecutor: ThreadPoolExecutor) {
    this()
    _serverMap = serverMap
    _threadPoolExe = threadPoolExecutor
  }

  def handle(msg: RpcRequest): Object = {
    val className = msg.className
    val version = msg.version
    val serverKey = ServerUtil.makeServiceKey(className, version)
    val obj: Object = _serverMap.get(serverKey)
    val clazz: Class[_] = obj.getClass
    val methodName = msg.methodName
    val parameterTypes: Array[Class[_]] = msg.parameterTypes
    val array: Array[Object] = msg.parameters
    val serviceFastClass = FastClass.create(clazz)
    val methodIndex = serviceFastClass.getIndex(methodName, parameterTypes)
    serviceFastClass.invoke(methodIndex, obj, array)
  }

  override def channelRead0(ctx: ChannelHandlerContext, msg: RpcRequest): Unit = {
    /**
     * TODO
     * filter heartBeat
     */

    _threadPoolExe.execute(() => {
      val response = new RpcResponse
      response.requestId = msg.requestId
      val result = handle(msg)
      response.result = result
      ctx.writeAndFlush(response).addListener((channelFuture: ChannelFuture) => {
      })
    })


  }

  /**
   * remote if close
   *
   * @param ctx
   * @param evt
   */
  override def userEventTriggered(ctx: ChannelHandlerContext, evt: Any): Unit = {
    if (evt.isInstanceOf[IdleStateEvent]) {
      ctx.channel().close()
    } else {
      super.userEventTriggered(ctx, evt)
    }
  }
}
