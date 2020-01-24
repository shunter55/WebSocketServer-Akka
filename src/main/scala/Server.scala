import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Flow

import scala.concurrent.Future
import scala.io.StdIn

/**
 * TODO: add description
 *
 * @author stephen.kobata
 * @since Jan-2020
 */
object Server extends App {
  val server = new Server()

  server()
  println("Done")
}

class Server {
  implicit val actorSystem: ActorSystem = ActorSystem("akka-system")
  implicit val flowMaterializer: ActorMaterializer = ActorMaterializer()

  val interface = "localhost"
  val port = 8080

  def apply(): Unit = {
    val route: Route = getRoute()

    val binding: Future[Http.ServerBinding] = Http().bindAndHandle(route, interface, port)

    println(s"Server is now online at http://$interface:$port\nPress RETURN to stop...")

    StdIn.readLine()

    import actorSystem.dispatcher
    binding.flatMap(_.unbind()).onComplete(_ => actorSystem.terminate())

    println("Server is down...")
  }

  def echoService(): Flow[Message, Message, _] = {
    Flow[Message].map {
      case TextMessage.Strict(txt) => TextMessage("ECHO: " + txt)
      case _ => TextMessage("Message type unsupported.")
    }
  }

  def getRoute(): Route = {
    get {
      pathEndOrSingleSlash {
        complete("Welcome to websocket server!")
      }
    } ~
    path("ws-echo") {
      get {
        handleWebSocketMessages(echoService())
      }
    } ~
    pathPrefix("ws-chat" / IntNumber) {
      chatId: Int => {
        parameter('name) {
          userName: String =>
            handleWebSocketMessages(ChatRooms.findOrCreate(chatId).websocketFlow(userName))
        }
      }
    }

  }

}


