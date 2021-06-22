import java.util.Properties

import java.sql.{Connection, DriverManager}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object Producer {


  //kafka参数
  val topic = "mn_user_info"
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"

  def main(args: Array[String]): Unit = {
    val mysqlContent = readFile()
    produceToKafka(mysqlContent)
  }

  /**
   * 从mySQL中读取文件内容
   *
   */
  def readFile(): String = {
    val url = "jdbc:mysql://bigdata130.depts.bingosoft.net:24306//user29_db"
    val properties = new Properties()
    properties.setProperty("driverClassName", "com.mysql.jdbc.Driver")
    properties.setProperty("user", "user29")
    properties.setProperty("password", "pass@bingo29")


    var res = ""
    val connection = DriverManager.getConnection(url, properties)
    try {
    val statement = connection.createStatement
    println("表数据：")
    var resultSet = statement.executeQuery("select * from users")

    while (resultSet.next) {
      val a = resultSet.getString(0)
      val b = resultSet.getString(1)
      val c = resultSet.getString(2)
      //输出表数据

      res += "{\"username\":\"" + a + "\",  \"password\":\"" + b + "\", \"note\":\"" + c + "\"}\n"

    }
    resultSet.close()
  } catch {
    case e: Exception => e.printStackTrace()
  }
  return res
}

  /**
   * 把数据写入到kafka中
   *
   */
  def produceToKafka(s3Content: String): Unit = {
    val props = new Properties
    props.put("bootstrap.servers", bootstrapServers)
    props.put("acks", "all")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    val dataArr = s3Content.split("\n")
    for (s <- dataArr) {
      if (!s.trim.isEmpty) {
        val record = new ProducerRecord[String, String](topic, null, s)
        println("开始生产数据：" + s)
        producer.send(record)
      }
    }
    producer.flush()
    producer.close()
  }
}
