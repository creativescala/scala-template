package objective

import parsley.Parsley
import parsley.syntax.all.*
import parsley.syntax.character.*

import scala.concurrent.duration.*

enum Status:
  case Open
  case Closed

final case class Item(status: Status, description: String, duration: Duration)

object Parser:
  val description = parsley.character.stringOfSome(_ != ':')
  val separator: Parsley[String] = ": "
  val duration: Parsley[Duration] =
    parsley.character.stringOfSome(_.isDigit).map(_.toInt.minutes)

  val status: Parsley[Status] =
    "- [" *> (' '.map(_ => Status.Open) | 'X'.map(_ => Status.Closed)) <* "] "

  val itemLine: Parsley[Item] =
    Item.apply.lift(
      status,
      description <* separator,
      duration <* parsley.character.endOfLine
    )

  val otherLine: Parsley[Unit] =
    (parsley.character.stringOfMany(
      _ != '\n'
    ) <* parsley.character.endOfLine).void

  val line = itemLine.map(Some.apply).orElse(otherLine.as(None))

  val lines = line.foldRight(List.empty)((maybeLine, accum) =>
    maybeLine match
      case None       => accum
      case Some(line) => line :: accum
  )
