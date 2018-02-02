package thesis

import java.io.{BufferedReader, InputStreamReader, LineNumberReader}

/**
  * Created by liuziwei on 2017/11/3.
  */
object Test {

  def main( args:Array[String]) {
    try {
      System.out.println("start")
      val pr: Process = Runtime.getRuntime().exec("python E:\\workspace\\test\\main.py")
      val in: BufferedReader = new BufferedReader(new InputStreamReader(pr.getInputStream()))
      var line: String = in.readLine()
      while (line!=null && line.length > 0) {
        System.out.println(line)
        line = in.readLine()
      }
      in.close()
     // pr.waitFor
      System.out.println("end")
    }
    catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }
  }

}
