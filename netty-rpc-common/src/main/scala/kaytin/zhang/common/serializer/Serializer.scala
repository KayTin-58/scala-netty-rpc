package kaytin.zhang.common.serializer

trait Serializer {

  /**
   * serialize
   *
   * @param obj obj
   * @tparam T t
   * @return
   */
   def serialize[T](obj: T): Array[Byte]

  /**
   * deserialize
   *
   * @param bytes bytes
   * @param clazz clazz
   * @tparam T
   */
   def deserialize[T](bytes: Array[Byte], clazz: Class[T]):Object
}
