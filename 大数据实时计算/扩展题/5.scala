import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import scala.io.StdIn

/*
一开始还以为是如何在运行过程中改变关键字。。。
*/

class ThreadExample extends Thread{
  var target = "b"

 def this(t: String){
    this()
    target = t
  }

  def count(char: String, string: String): Int = {
    return string.count(_ == char)
  }

  override def run(){
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //Linux or Mac:nc -l 9999
    //Windows:nc -l -p 9999
    val text = env.socketTextStream("localhost", 9999)
    val t = target
    val stream = text.flatMap {
      _.toLowerCase.split("\\W+") filter {
        _.contains(t)
      }
    }
    val printStream = stream.map {
      ("发现目标："+_)
    }
    printStream.print()
    env.execute("Window Stream WordCount")
  }
}

object Main {




  def main(args: Array[String]) {
      var target="b"
      println("输入你要监控的关键字:")
      val kw = StdIn.readLine()
      target = kw
      var t = new ThreadExample(target)        // 新建一个线程

      t.start()                          // 启动线程




  }
}
