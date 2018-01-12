package thesis.core

import akka.actor.Actor.Receive
import akka.actor._
import akka.http.scaladsl.model.ws.TextMessage
import org.slf4j._
import thesis.core.Node.WsClosed
import thesis.Boot.executor

import scala.concurrent.duration._
/**
  * Created by liuziwei on 2017/12/26.
  */
object Heart {
  trait Command
  case class Register(outActor:ActorRef) extends Command
  case object SendHeart extends Command

  def props = Props[Heart](new Heart)
}

class Heart extends Actor {

  import Heart._

  private val log = LoggerFactory.getLogger(this.getClass)



  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info(s"${context.self.path} starting...")
  }

  override def receive: Receive = idle

  def idle:Receive = {
    case Register(outer) =>
      log.info("heart beat start ...")
      val sch = context.system.scheduler.schedule(1 second,5 second,self,SendHeart)
      context.become(work(outer,sch))
      context.watch(outer)
  }

  def work(outer:ActorRef,sch:Cancellable):Receive = {
    case SendHeart =>
      outer ! TextMessage.Strict("heart beat")
    case Terminated(child) =>
      log.error(s"${child.path} dead...")
      sch.cancel()
      context.parent ! WsClosed
      context.become(idle)
  }
}
