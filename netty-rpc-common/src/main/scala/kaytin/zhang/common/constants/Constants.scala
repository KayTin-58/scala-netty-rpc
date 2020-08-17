package kaytin.zhang.common.constants

object Constants {

  val ZK_SESSION_TIMEOUT = 5000
  val ZK_CONNECTION_TIMEOUT = 5000

  val ZK_REGISTRY_PATH = "/registry"
  val ZK_DATA_PATH: String = ZK_REGISTRY_PATH + "/data"

  val ZK_NAMESPACE = "netty-rpc"
}
