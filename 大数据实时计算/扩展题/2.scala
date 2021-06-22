import java.util.{Properties, UUID}

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010

object Main {
  /**
   * 输入的主题名称
   */
  val inputTopic = "mn_buy_ticket_1"
  /**
   * kafka地址
   */
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"

  def main(args: Array[String]): Unit = {
    var destMap:Map[String, Int] = Map()
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val kafkaProperties = new Properties()
    kafkaProperties.put("bootstrap.servers", bootstrapServers)
    kafkaProperties.put("group.id", UUID.randomUUID().toString)
    kafkaProperties.put("auto.offset.reset", "earliest")
    kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    val kafkaConsumer = new FlinkKafkaConsumer010[String](inputTopic,
      new SimpleStringSchema, kafkaProperties)
    kafkaConsumer.setCommitOffsetsOnCheckpoints(true)
    val inputKafkaStream = env.addSource(kafkaConsumer)
    val result = inputKafkaStream.map(x => {
      println("======================")
      var dest = x.split(",")(3).split("\"")(3)
      if (destMap.contains(dest)){
        val nv = destMap(dest) + 1
        destMap -= dest
        destMap += (dest -> nv)
      }else {
        destMap += (dest -> 1)
      }
      destMap.toArray.sortBy(-_._2).take(5).foreach{
        case(str, i) => println(str + ":" + i)
      }
    })
    env.execute()
  }
}
