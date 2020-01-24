import akka.actor.ActorSystem

/**
 * TODO: add description
 *
 * @author stephen.kobata
 * @since Jan-2020
 */
object ChatRooms {

  var chatRooms: Map[Int, ChatRoom] = Map.empty

  def findOrCreate(number: Int)(implicit actorSystem: ActorSystem): ChatRoom = {
    chatRooms.getOrElse(number, createNewChatRoom(number))
  }

  private def createNewChatRoom(number: Int)(implicit actorSystem: ActorSystem): ChatRoom = {
    val chatroom = ChatRoom(number)
    chatRooms += number -> chatroom
    chatroom
  }

}
