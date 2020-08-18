package kaytin.zhang.common.loadbalance.impl

import java.util
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import kaytin.zhang.common.loadbalance.RpcLoadBalance
import kaytin.zhang.common.pojo.RpcProtocol
import org.jboss.netty.channel.SimpleChannelHandler
import org.springframework.util.CollectionUtils

import scala.collection.mutable.ListBuffer

/**
 * lfu:最少最近使用
 *
 * LRU，最近最少使用，把数据加入一个链表中，按访问时间排序，发生淘汰的时候，把访问时间最旧的淘汰掉。
 * 比如有数据 1，2，1，3，2
 * 此时缓存中已有（1，2）
 * 当3加入的时候，得把后面的2淘汰，变成（3，1）
 *
 * LFU，最近不经常使用，把数据加入到链表中，按频次排序，一个数据被访问过，把它的频次+1，发生淘汰的时候，把频次低的淘汰掉。
 * 比如有数据 1，1，1，2，2，3
 * 缓存中有（1(3次)，2(2次)）
 * 当3加入的时候，得把后面的2淘汰，变成（1(3次)，3(1次)）
 * 区别：LRU 是得把 1 淘汰。
 *
 * 作者：ck2016
 * 链接：https://www.jianshu.com/p/1f8e36285539
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
class LfuRpcLoadBalance extends RpcLoadBalance {
  /**
   * 缓存容器
   */
  private val jobLfuMap: ConcurrentMap[String, util.HashMap[RpcProtocol, Integer]] =
    new ConcurrentHashMap[String, util.HashMap[RpcProtocol, Integer]]
  var CACHE_VALID_TIME: Long = 0

  /**
   * routine
   *
   * @param serviceKey serviceKey
   * @return SimpleChannelHandler
   */
  override def routine(serviceKey: String, serverNodes: Map[RpcProtocol, SimpleChannelHandler]): SimpleChannelHandler = {
    initServerNodes(serverNodes)
    serverNodes(doRoutine(serviceKey))
  }

  /**
   * lfu算法实现
   *
   * @param serverKey
   * @return
   */
  def doRoutine(serverKey: String): RpcProtocol = {
    val rpcProtocols: ListBuffer[RpcProtocol] = _serverNodes(serverKey)
    // 缓存失效
    if (System.currentTimeMillis() > CACHE_VALID_TIME) {
      jobLfuMap.clear()
      CACHE_VALID_TIME = System.currentTimeMillis() + 1000 * 60 * 60 * 24L
    }

    // init
    var lfuItemMap: util.HashMap[RpcProtocol, Integer] = jobLfuMap.get(serverKey)
    if (CollectionUtils.isEmpty(lfuItemMap)) {
      lfuItemMap = new util.HashMap[RpcProtocol, Integer]()
      jobLfuMap.putIfAbsent(serverKey, lfuItemMap)
    }

    // put new
    val rpcProtocolList = _serverNodes.get(serverKey)
    rpcProtocolList.foreach(o => {
      if (!lfuItemMap.containsKey(o) && lfuItemMap.get(o) > 1000000) {
        lfuItemMap.put(o, 0)
      }
    })

    // 淘汰失效的
    val iterator = lfuItemMap.entrySet().iterator()
    while (iterator.hasNext) {
      val item = iterator.next()
      if (!rpcProtocolList.contains(item.getKey)) {
        iterator.remove()
      }
    }

    // load least used count address
    val lfuItemList: util.List[util.Map.Entry[RpcProtocol, Integer]] = new util.ArrayList[util.Map.Entry[RpcProtocol, Integer]](lfuItemMap.entrySet)
    lfuItemList.stream().sorted((o1: util.Map.Entry[RpcProtocol, Integer],
                                 o2: util.Map.Entry[RpcProtocol, Integer]) => o1.getValue.compareTo(o2.getValue))
    // chose it
    val mapEntity = lfuItemList.get(0)
    //++
    mapEntity.setValue(mapEntity.getValue + 1)
    mapEntity.getKey
  }

}


