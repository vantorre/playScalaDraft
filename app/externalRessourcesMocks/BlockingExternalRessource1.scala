package externalRessourcesMocks;
import akka.actor._

class BlockingExternalRessource1 extends Actor {
  def receive = {
    case i: Int ⇒
      Thread.sleep(5000) //block for 5 seconds, representing blocking I/O, etc
      println(s"Blocking operation finished: ${i}")
      sender() ! "ressource1"
  }
}