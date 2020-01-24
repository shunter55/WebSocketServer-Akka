import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.model.ws.Message
import akka.stream.scaladsl.Flow

/**
 * TODO: add description
 *
 * @author stephen.kobata
 * @since Jan-2020
 */
object ChatRoom {
  def apply(roomId: Int)(implicit actorSystem: ActorSystem) = new ChatRoom(roomId, actorSystem)
}

class ChatRoom(roomId: Int, actorSystem: ActorSystem) {

  private val chatRoomActor = actorSystem.actorOf(Props(classOf[ChatRoomActor], roomId))

  def websocketFlow(user: String): Flow[Message, Message, _] = ???

  def sendMessage(message: ChatMessage): Unit = chatRoomActor ! message

}
