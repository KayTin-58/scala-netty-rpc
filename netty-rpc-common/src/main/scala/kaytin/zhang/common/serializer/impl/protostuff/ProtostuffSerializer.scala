package kaytin.zhang.common.serializer.impl.protostuff

import kaytin.zhang.common.serializer.Serializer

class ProtostuffSerializer extends Serializer{
  /**
   * serialize
   *
   * @param obj obj
   * @tparam T t
   * @return
   */
  override def serialize[T](obj: T): Array[Byte] = {

  }

  /**
   * deserialize
   *
   * @param bytes bytes
   * @param clazz clazz
   * @tparam T
   */
  override def deserialize[T](bytes: Array[Byte], clazz: Class[T]): AnyRef = {

  }
}
