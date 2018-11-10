package externalRessourcesMocks;
import akka.actor._

class BlockingExternalRessource2 extends Actor {
  def receive = {
    case i: Int ⇒
      Thread.sleep(4000) //block for 4 seconds, representing blocking I/O, etc
      println(s"Blocking operation2 finished: ${i}")
      sender() ! "ressource2"
  }
}