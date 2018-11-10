package externalRessourcesMocks;
import javax.inject._
import akka.actor._
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration._

import play.api.libs.concurrent.Akka

class BlockingExternalRessourceSync1 @Inject()(actorSystem: ActorSystem)(implicit ec: ExecutionContext) extends Actor {
  def receive = {
    case i: Int ⇒
      Thread.sleep(5000) //block for 5 seconds, representing blocking I/O, etc
      println(s"Blocking operation finished: ${i}")
      sender() ! "ressource1"
  }

}