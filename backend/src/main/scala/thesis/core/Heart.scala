package thesis.core

import akka.actor.Actor.Receive
import akka.actor._
import org.slf4j._
/**
  * Created by liuziwei on 2017/12/26.
  */
object Heart {
  trait Command
  case class Register(outActor:ActorRef) extends Command

  def props = Props[Mission](new Mission)
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
      context.become(work(outer))
      context.watch(outer)
  }

  def work(outer:ActorRef):Receive = {
    case Terminated(child) =>
      log.error(s"${child.path} dead...")
      context.become(idle)
  }
}
