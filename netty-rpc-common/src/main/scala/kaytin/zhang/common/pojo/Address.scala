package kaytin.zhang.common.pojo

case class Address(host: String, port: Int) {
  override def toString: String ={
     this.host+":"+this.port
  }
}
