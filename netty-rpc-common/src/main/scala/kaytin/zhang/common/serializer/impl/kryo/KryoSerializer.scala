package kaytin.zhang.common.serializer.impl.kryo
import com.esotericsoftware.kryo.Kryo
import kaytin.zhang.common.serializer.Serializer

class KryoSerializer extends Serializer{
  private val pool = KryoPoolFactory.getKryoPoolInstance
  /**
   * serialize
   *
   * @param obj obj
   * @tparam T t
   * @return
   */
  override def serialize[T](obj: T): Array[Byte] = {
    val kryo:Kryo = pool.borrow()

  }

  /**
   * deserialize
   *
   * @param bytes bytes
   * @param clazz clazz
   * @tparam T
   */
  override def deserialize[T](bytes: Array[Byte], clazz: Class[T]): AnyRef = ???
}
