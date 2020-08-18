package kaytin.zhang.client.discover

import kaytin.zhang.client.connect.ConnectionManager
import kaytin.zhang.common.constants.Constants
import kaytin.zhang.common.pojo.{Address, RpcProtocol}
import kaytin.zhang.common.zkclient.CuratorClient
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent

import scala.collection.mutable.ListBuffer

/**
 * des:监听服务
 */
class ServiceDiscover {

  var _curatorClient: CuratorClient = _

  def this(zkAddress: Address) {
    this()
    this._curatorClient = new CuratorClient(zkAddress.host + ":" + zkAddress.port)
    discoverService
  }

  /**
   * 服务发现
   */
  def discoverService: Unit = {
    getAndUpdateServiceList
    _curatorClient.watchPathChildrenNode(Constants.ZK_REGISTRY_PATH,
      (curatorFramework: CuratorFramework, pathChildrenCacheEvent: PathChildrenCacheEvent) => {
        val typeL = PathChildrenCacheEvent.Type
        pathChildrenCacheEvent.getType match {
          case typeL.CONNECTION_RECONNECTED => getAndUpdateServiceList
          case typeL.CHILD_ADDED =>
          case typeL.CHILD_UPDATED =>
          case typeL.CHILD_UPDATED => getAndUpdateServiceList
        }
      })
  }

  /**
   * get and update service
   */
  def getAndUpdateServiceList: Unit = {
    val pathList = _curatorClient.getChildren(Constants.ZK_REGISTRY_PATH)
    val nodeList = new ListBuffer[RpcProtocol]()
    pathList.forEach(o => {
      val bytes = _curatorClient.getData(o)
      val json = new String(bytes)
      val rpcProtocol: RpcProtocol = RpcProtocol.fromJson(json)
      nodeList.+=(rpcProtocol)
      // ConnectionManager TODO
      ConnectionManager.getInstance.setServerNodes(nodeList)
    })
  }
}
