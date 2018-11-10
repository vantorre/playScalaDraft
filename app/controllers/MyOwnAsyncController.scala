package controllers

import javax.inject._

import akka.actor.ActorSystem
import play.api.mvc._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

import externalRessourcesMocks.BlockingExternalRessource1

import akka.pattern.ask
import akka.actor._
import akka.util.Timeout




@Singleton
class MyOwnAsyncController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {
  implicit val timeout: Timeout = 10 seconds

  def message = Action.async {
    val ressourceActor = actorSystem.actorOf(Props[BlockingExternalRessource1], "res1Actor")
    (ressourceActor ? 1).mapTo[String].map { message =>
      Ok(message)
    }
  }


}
