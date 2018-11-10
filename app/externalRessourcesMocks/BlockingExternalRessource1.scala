package externalRessourcesMocks;
import javax.inject._
import akka.actor._
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration._

import play.api.libs.concurrent.Akka

class BlockingExternalRessource1 @Inject()(actorSystem: ActorSystem)(implicit ec: ExecutionContext) extends Actor {
  def receive = {
    case i: Int ⇒
      val send = sender()
      getFutureMessage(5.second).map { msg => {
        println(s"Blocking operation1 finished: ${i}" + msg)
        send ! msg
      } }
    case s: String ⇒ println("j ai recu une string")
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success("ressource1")
    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }

}