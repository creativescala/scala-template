package objective

import cats.effect.IO
import cats.effect.IOApp
import fs2.Stream

object Objective extends IOApp.Simple:
  val run =
    Stream
      .exec(IO.println("Starting objective"))
      .append(Server.server)
      .append(Stream.exec(IO.println("Stopping objective")))
      .compile
      .drain
