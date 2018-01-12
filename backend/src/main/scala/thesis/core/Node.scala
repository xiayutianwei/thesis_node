package thesis.core

import akka.actor.Actor.Receive
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage, WebSocketRequest}
import akka.stream.{ActorAttributes, Materializer, OverflowStrategy, Supervision}
import akka.stream.scaladsl._
import akka.actor._
import akka.http.scaladsl.model.ws._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.stream.Supervision.Decider
import akka.util.ByteStringBuilder
import org.slf4j._
import io.circe.generic.auto._
import io.circe.syntax._
import thesis.common.AppSettings._
import thesis.Boot._
import thesis.core.Heart.Register

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration._
/**
  * Created by liuziwei on 2017/12/26.
  */
object Node{
  sealed trait Command extends Heart.Command
  case object Start extends Command
  case object Work extends Command
  case object WsClosed extends Command
  def props = Props[Node](new Node)

  object Symbol{
    val Heart = "heart"
    val Mission = "mission"
  }
}

class Node extends Actor {

  import Node._


  private val log = LoggerFactory.getLogger(this.getClass)
  private val decider: Decider = {
    e: Throwable =>
      e.printStackTrace()
      println(s"WS stream failed with $e")
      Supervision.Resume
  }

  def getChild(symbol:String) = {
    context.child(symbol).getOrElse{
      symbol match{
        case Symbol.Heart =>
          context.actorOf(Heart.props)
        case Symbol.Mission =>
          context.actorOf(Mission.props)
      }
    }
  }

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info(s"node  starting...")
    self ! Start
  }

  override def receive: Receive = idle

  def idle:Receive = {
      case Start =>
        var outer:ActorRef = null
        val webSocketFlow1 = Http().webSocketClientFlow(WebSocketRequest(s"ws://$masterHost:$masterPort/master/node/subscribe?name=$nodeName"))
        val response =
          Source.actorRef(1024,OverflowStrategy.fail).mapMaterializedValue(outActor => outer = outActor)
            .viaMat(webSocketFlow1)(Keep.right)
            .toMat(Sink.actorRef[Message](getChild(Symbol.Mission),"stop"))(Keep.left)
            .run()
        //  val closed = webSocketFlow1.watchTermination().andThen()
        val connected = response.flatMap { upgrade =>
          if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
            Future.successful("connect success")
          } else {
            throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
          }
        } //链接建立时
        connected.onComplete{
          case Success(i) =>
            log.info(s"connected failure:${i.toString}")
            getChild(Symbol.Heart) ! Register(outer)
            self ! Work
          case Failure(e) =>
            log.error(s"connected failure:${e.getMessage}")
            context.system.scheduler.scheduleOnce(5 minute,self,Start)
        }

      case Work =>
        context.become(work)
      case _ =>
  }

  def work:Receive = {
    case WsClosed =>
      context.become(idle)
      context.system.scheduler.scheduleOnce(5 minute,self,Start)
    case _ =>
  }
}
