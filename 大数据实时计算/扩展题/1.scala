import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object Main {
  val target="b"

  def count(char: String, string: String): Int = {
    return string.count(_ == char)
  }

  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //Linux or Mac:nc -l 9999
    //Windows:nc -l -p 9999
    val text = env.socketTextStream("localhost", 9999)
    val stream = text.flatMap {
      _.toLowerCase.split("\\W+") filter {
        _.contains(target)
      }
    }
    val printStream = stream.map {
      ("发现目标："+_)
    }
    val countStream = stream.map(item => count(target, item)).timeWindowAll(Time.seconds(60),Time.seconds(10)).sum(0)
    countStream.print()
    printStream.print()
    env.execute("Window Stream WordCount")
  }
}
