package kaytin.zhang.common.codec

object Beat {
  val BEAT_INTERVAL = 30
  val BEAT_TIMEOUT: Int = 3 * BEAT_INTERVAL
  val BEAT_ID = "BEAT_PING_PONG"

  val BEAT_PING: RpcRequest = new RpcRequest
  BEAT_PING.requestId = BEAT_ID
}
