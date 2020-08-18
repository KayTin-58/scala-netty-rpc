package kaytin.zhang.common

class RpcException(code: Int = 0, exeMsg: String) extends Throwable{
  override def toString: String = {
    "errorCode:" + code + ";errorMsg:" + exeMsg
  }
}
