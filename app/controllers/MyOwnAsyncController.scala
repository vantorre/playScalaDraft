package controllers

import javax.inject._

import akka.actor.ActorSystem
import play.api.mvc._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

import externalRessourcesMocks.BlockingExternalRessource1
import externalRessourcesMocks.BlockingExternalRessource2

import akka.pattern.ask
import akka.actor._
import akka.util.Timeout


@Singleton
class MyOwnAsyncController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {
  implicit val timeout: Timeout = 10 seconds

  def message = Action.async {
    val start = System.currentTimeMillis()
    def getLatency(m: String): String = m + "=" + (System.currentTimeMillis() - start) + "ms"

    //    bug lors d appel multiples a cause du nom de l acteur qui doit etre unique
    val ressourceActor1 = actorSystem.actorOf(Props[BlockingExternalRessource1], "res1Actor")
    val ressourceActor2 = actorSystem.actorOf(Props[BlockingExternalRessource2], "res2Actor")


    val r1 = (ressourceActor1 ? 1).mapTo[String].map(getLatency)
    val r2 = (ressourceActor2 ? 2).mapTo[String].map(getLatency)

    r1.flatMap { r1Response: String =>
      r2.map { r2Response: String =>
        Ok(r1Response + "/" + r2Response)
      }
    }
  }


}
