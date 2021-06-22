import java.util.{Properties, UUID}

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer010}

object Consumer {
  val bucket = "wuhongming"
  val accessKey = "294D4C03E0EB6B66040F"
  val secretKey = "W0REODlBRjgzOTU1NEVENDRBMTE1MjY0QTg3NzYy"
  val endpoint = "http://10.16.0.1:81"

  //要读取的文件
  val key = "input.txt"
  //上传文件的路径前缀
  val keyPrefix = "upload/"
  //上传数据间隔 单位毫秒
  val period = 5000
  //输入的kafka主题名称
  val inputTopic = "mn_buy_ticket_6"
  //kafka地址
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
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
    val processedStream = inputKafkaStream.flatMap{
      _.split("\n") filter {
        _.contains("吴鸿铭")
      }
    }
    processedStream.writeUsingOutputFormat(new S3Writer(accessKey, secretKey, endpoint, bucket, keyPrefix, period))
    env.execute()
  }
}
