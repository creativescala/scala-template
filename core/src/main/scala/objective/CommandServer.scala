package objective

import cats.effect.IO
import com.comcast.ip4s.UnixSocketAddress
import fs2.Stream
import fs2.io.net.*
import fs2.text

object CommandServer:

  val connections: Stream[IO, Socket[IO]] = Network.forIO.bindAndAccept(
    Configuration.commandSocketAddress,
    List(
      SocketOption.unixSocketDeleteIfExists(true),
      SocketOption.unixSocketDeleteOnClose(true)
    )
  )

  val server =
    connections.map(socket =>
      socket.reads.through(text.utf8.decode).through(text.lines).evalMap {
        case "reload"  => IO.println("Reload")
        case "inspect" => IO.println("Inspect")
        case other     => IO.println(s"Unknown command: $other")
      }
    )
