package kaytin.zhang.common.zkclient

import java.util

import kaytin.zhang.common.constants.Constants
import org.apache.curator.framework.recipes.cache.{PathChildrenCache, PathChildrenCacheListener, TreeCache, TreeCacheListener}
import org.apache.curator.framework.state.ConnectionStateListener
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.zookeeper.{CreateMode, Watcher}

class CuratorClient {
  var client: CuratorFramework = _

  def this(connectString: String, nameSpace: String, sessionTimeOut: Int, connectionTimeout: Int) {
    /**
     * 必须调用主构造器
     */
    this()
    client = CuratorFrameworkFactory.builder
      .namespace(nameSpace)
      .connectString(connectString)
      .sessionTimeoutMs(sessionTimeOut)
      .connectionTimeoutMs(connectionTimeout)
      .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build;
  }

  def this(connectString: String, timeOut: Int) {
    this(connectString, Constants.ZK_NAMESPACE, timeOut, timeOut)
  }

  def this(connectString: String) {
    this(connectString, 5000)
  }

  def getClient: CuratorFramework = client

  def addConnectionStateListener(connectionStateListener: ConnectionStateListener): Unit = {
    client.getConnectionStateListenable.addListener(connectionStateListener)
  }

  @throws[Exception]
  def createPathData(path: String, data: Array[Byte]): Unit = {
    client.create.creatingParentsIfNeeded.withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, data)
  }

  @throws[Exception]
  def updatePathData(path: String, data: Array[Byte]): Unit = {
    client.setData.forPath(path, data)
  }

  @throws[Exception]
  def deletePath(path: String): Unit = {
    client.delete.forPath(path)
  }

  @throws[Exception]
  def watchNode(path: String, watcher: Watcher): Unit = {
    client.getData.usingWatcher(watcher).forPath(path)
  }

  @throws[Exception]
  def getData(path: String): Array[Byte] = client.getData.forPath(path)

  @throws[Exception]
  def getChildren(path: String): util.List[String] = client.getChildren.forPath(path)

  def watchTreeNode(path: String, listener: TreeCacheListener): Unit = {
    val treeCache = new TreeCache(client, path)
    treeCache.getListenable.addListener(listener)
  }

  @throws[Exception]
  def watchPathChildrenNode(path: String, listener: PathChildrenCacheListener): Unit = {
    val pathChildrenCache = new PathChildrenCache(client, path, true)
    /**
     * BUILD_INITIAL_CACHE 代表使用同步的方式进行缓存初始化。
     */
    pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE)
    pathChildrenCache.getListenable.addListener(listener)
  }

  def close(): Unit = {
    client.close()
  }
}
