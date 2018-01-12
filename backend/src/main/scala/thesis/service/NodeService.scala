package thesis.service

import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, RequestContext}
import thesis.core.Node
import akka.pattern.ask
/**
  * Created by liuziwei on 2018/1/3.
  */
trait NodeService extends BaseService{
  import io.circe.generic.auto._


}
