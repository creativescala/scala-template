package objective

import com.comcast.ip4s.UnixSocketAddress

object Configuration:
  val maxConcurrentConnections = 5
  val commandSocketAddress = UnixSocketAddress(
    "/run/user/1000/objective-command.sock"
  )
  val clientSocketAddress = UnixSocketAddress(
    "/run/user/1000/objective-client.sock"
  )
