package kaytin.zhang.common.serializer.impl.kryo

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.pool.{KryoFactory, KryoPool}
import kaytin.zhang.common.codec.{RpcRequest, RpcResponse}
import org.objenesis.strategy.StdInstantiatorStrategy

class KryoPoolFactory private {

  private val factory: KryoFactory = new KryoFactory() {
    override def create: Kryo = {
      val kryo = new Kryo
      kryo.setReferences(false)
      kryo.register(classOf[RpcRequest])
      kryo.register(classOf[RpcResponse])
      val strategy = kryo.getInstantiatorStrategy.asInstanceOf[Kryo.DefaultInstantiatorStrategy]
      strategy.setFallbackInstantiatorStrategy(new StdInstantiatorStrategy)
      kryo
    }
  }
  val pool: KryoPool = new KryoPool.Builder(factory).build

  def getPool: KryoPool = {
    pool
  }
}


/**
 * single model
 */
object KryoPoolFactory {
  val kryoPoolFactory = new KryoPoolFactory

  def getKryoPoolInstance: KryoPool = {
    kryoPoolFactory.getPool
  }
}
