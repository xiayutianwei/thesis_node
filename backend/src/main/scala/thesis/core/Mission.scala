package thesis.core

import akka.actor.Actor.Receive
import akka.actor._
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import org.slf4j._
import thesis.common.AppSettings._
import thesis.Boot.executor
/**
  * Created by liuziwei on 2017/12/26.
  */
object Mission {

  trait Command

  def props = Props[Mission](new Mission)



}

class Mission extends Actor {
  import Mission._

  private var lastTime = 0l

  private val log = LoggerFactory.getLogger(this.getClass)

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info(s"${context.self.path} starting...")
  }

  override def receive: Receive = idle

  def idle:Receive =  {
    case msg:Message =>
      msg match{
        case str:TextMessage =>
          log.info(s"$nodeName receive mission $str")
        case _:BinaryMessage =>
      }
  }
}
