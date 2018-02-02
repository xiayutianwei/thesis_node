package thesis

import java.io.{BufferedReader, InputStreamReader, LineNumberReader}

/**
  * Created by liuziwei on 2017/11/3.
  */
object Test {

  def main( args:Array[String]) {
    try {
      System.out.println("start")
      val env = Array[String]("PATH=" + System.getenv("PATH"),
        "JAVA_HOME=" + System.getenv("JAVA_HOME"),
        "LD_LIBRARY_PATH=" + "./:" + System.getenv("LD_LIBRARY_PATH") + ":" + System.getenv("JAVA_HOME") + "/jre/lib/amd64/server" ,
        "CLASSPATH=" + "./:" + System.getenv("CLASSPATH") + ":" + System.getProperty("java.class.path"),
        "PYTHONUNBUFFERED=1")
      val pb=new ProcessBuilder()
      val pr: Process = Runtime.getRuntime.exec(args(0))
      val in: BufferedReader = new BufferedReader(new InputStreamReader(pr.getInputStream()))
      var line: String = in.readLine()
      while (line!=null && line.length > 0) {
        System.out.println(line)
        line = in.readLine()
      }
      in.close()
      pr.waitFor
      println(pr.exitValue())
      System.out.println("end")
    }
    catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }
  }

}
