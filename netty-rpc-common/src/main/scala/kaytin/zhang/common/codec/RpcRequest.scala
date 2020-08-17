package kaytin.zhang.common.codec

/**
 * RpcRequest
 */
class RpcRequest extends Serializable {
  var requestId: String = _
  var className: String = _
  var methodName: String = _
  var parameterTypes: Array[Class[_]] = _
  var parameters: Array[Object] = _
  var version: String = _
}
