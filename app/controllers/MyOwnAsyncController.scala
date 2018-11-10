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



class MyOwnAsyncController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {
  implicit val timeout: Timeout = 10 seconds
  //    bug lors d appel multiples a cause du nom de l acteur qui doit etre unique
  val ressourceActor1 = actorSystem.actorOf(Props(new BlockingExternalRessource1(actorSystem)), "res1Actor")
  val ressourceActor2 = actorSystem.actorOf(Props(new BlockingExternalRessource2(actorSystem)), "res2Actor")
  implicit val ec = actorSystem.dispatchers.lookup("myown-context")

  def message = Action.async {
    println(s"MyOwnAsyncController catch request")
    val start = System.currentTimeMillis()
    def getLatency(m: String): String = m + "=" + (System.currentTimeMillis() - start) + "ms"



    val r1 = (ressourceActor1 ? 1).mapTo[String].map(getLatency)
    val r2 = (ressourceActor2 ? 2).mapTo[String].map(getLatency)

    r1.flatMap { r1Response: String =>
      r2.map { r2Response: String =>
        println(s"MyOwnAsyncController response request")
        Ok(r1Response + "/" + r2Response)
      }
    }
  }


}
