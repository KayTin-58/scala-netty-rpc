package kaytin.zhang.common.codec

/**
 * RpcResponse
 */
class RpcResponse extends Serializable {
  val serialVersionUID = 8215493329459772524L
  var requestId: String = _
  var error: String = _
  var result: Any = _
}
