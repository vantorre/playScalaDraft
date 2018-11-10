package controllers

import javax.inject._

import akka.actor.ActorSystem
import play.api.mvc._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise, Await}

import externalRessourcesMocks.BlockingExternalRessourceSync1
import externalRessourcesMocks.BlockingExternalRessourceSync2

import akka.pattern.ask
import akka.actor._
import akka.util.Timeout


class MyOwnSyncController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {
  implicit val timeout: Timeout = 20 seconds
  //    bug lors d appel multiples a cause du nom de l acteur qui doit etre unique
  val ressourceActor1 = actorSystem.actorOf(Props(new BlockingExternalRessourceSync1(actorSystem)), "ress1Actor")
  val ressourceActor2 = actorSystem.actorOf(Props(new BlockingExternalRessourceSync2(actorSystem)), "ress2Actor")
  implicit val ec = actorSystem.dispatchers.lookup("myown-context")

  def message = Action {
    println(s"MyOwnSyncController catch request")

    val start = System.currentTimeMillis()
    def getLatency(m: String): String = m + "=" + (System.currentTimeMillis() - start) + "ms"

    val r1 = Await.result((ressourceActor1 ? 1).mapTo[String], 18.second)
    val res1= getLatency(r1)
    val r2 = Await.result((ressourceActor2 ? 2).mapTo[String], 19.second)
    val res2= getLatency(r2)
    println(s"MyOwnAsyncController response request")
    Ok(res1 + "/" + res2)
  }
}




