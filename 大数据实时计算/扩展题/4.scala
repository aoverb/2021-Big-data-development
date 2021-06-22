import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import java.sql.{Connection, DriverManager}
import java.util.Properties

import scala.util.parsing.json._

object Main {
  def regJson(json:Option[Any]) = json match {
        case Some(map: Map[String, Any]) => map
  }
  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //Linux or Mac:nc -l 9999
    //Windows:nc -l -p 9999
    val text = env.socketTextStream("localhost", 9999)
    val stream = text.flatMap {
      _.toLowerCase.split("\\W+")
    }
    var x = "";
    val printStream = stream.map {
        _ => x
          val jsonS = JSON.parseFull(x)
          val first = regJson(jsonS)
          val url = "jdbc:mysql://bigdata130.depts.bingosoft.net:24306//user29_db"
          val properties = new Properties()
          properties.setProperty("driverClassName", "com.mysql.jdbc.Driver")
          properties.setProperty("user", "user29")
          properties.setProperty("password", "pass@bingo29")


          val connection = DriverManager.getConnection(url, properties)
          try {
            val statement = connection.createStatement
            val query = "insert into users(username, password, note) (\""+ first.get("username").toString.replace("Some(","").replace(")","") +"\", \""+ first.get("password").toString.replace("Some(","").replace(")","") +"\" \""+ first.get("note").toString.replace("Some(","").replace(")","") +"\" )"
            statement.execute(query)
          } catch {
            case e: Exception => e.printStackTrace()
          }
    }

    env.execute("sync with SQL")
  }
}
